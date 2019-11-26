package com.aiqin.mgs.order.api.service.impl;

import com.aiqin.mgs.order.api.base.exception.BusinessException;
import com.aiqin.mgs.order.api.component.enums.ErpOrderStatusEnum;
import com.aiqin.mgs.order.api.component.enums.PayStatusEnum;
import com.aiqin.mgs.order.api.config.properties.UrlProperties;
import com.aiqin.mgs.order.api.dao.OrderStoreOrderPayDao;
import com.aiqin.mgs.order.api.domain.AuthToken;
import com.aiqin.mgs.order.api.domain.OrderStoreOrderInfo;
import com.aiqin.mgs.order.api.domain.OrderStoreOrderPay;
import com.aiqin.mgs.order.api.domain.constant.OrderConstant;
import com.aiqin.mgs.order.api.domain.response.OrderPayResultResponse;
import com.aiqin.mgs.order.api.service.ErpOrderOperationService;
import com.aiqin.mgs.order.api.service.ErpOrderPayService;
import com.aiqin.mgs.order.api.service.ErpOrderQueryService;
import com.aiqin.mgs.order.api.util.AuthUtil;
import com.aiqin.mgs.order.api.util.OrderPublic;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Service
public class ErpOrderPayServiceImpl implements ErpOrderPayService {

    private static final Logger logger = LoggerFactory.getLogger(ErpOrderPayServiceImpl.class);

    @Resource
    private ErpOrderQueryService erpOrderQueryService;

    @Resource
    private ErpOrderOperationService erpOrderOperationService;

    @Resource
    private OrderStoreOrderPayDao orderStoreOrderPayDao;

    @Resource
    private UrlProperties urlProperties;

    @Override
    public OrderStoreOrderPay getOrderPayByOrderId(String orderId) {
        OrderStoreOrderPay orderPay = null;
        if (StringUtils.isNotEmpty(orderId)) {
            OrderStoreOrderPay orderPayQuery = new OrderStoreOrderPay();
            orderPayQuery.setOrderId(orderId);
            List<OrderStoreOrderPay> select = orderStoreOrderPayDao.select(orderPayQuery);
            if (select != null && select.size() > 0) {
                orderPay = select.get(0);
            }
        }
        return orderPay;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrderPay(OrderStoreOrderPay orderStoreOrderPay, AuthToken auth) {
        orderStoreOrderPay.setCreateById(auth.getPersonId());
        orderStoreOrderPay.setCreateByName(auth.getPersonName());
        orderStoreOrderPay.setUpdateById(auth.getPersonId());
        orderStoreOrderPay.setUpdateByName(auth.getPersonName());
        Integer insert = orderStoreOrderPayDao.insert(orderStoreOrderPay);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateOrderPaySelective(OrderStoreOrderPay orderStoreOrderPay, AuthToken auth) {
        orderStoreOrderPay.setUpdateById(auth.getPersonId());
        orderStoreOrderPay.setUpdateByName(auth.getPersonName());
        Integer integer = orderStoreOrderPayDao.updateByPrimaryKeySelective(orderStoreOrderPay);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void orderPay(OrderStoreOrderInfo orderStoreOrderInfo) {

        AuthToken auth = AuthUtil.getCurrentAuth();
        if (auth.getPersonId() == null || "".equals(auth.getPersonId())) {
            throw new BusinessException("请先登录");
        }

        if (orderStoreOrderInfo == null) {
            throw new BusinessException("空参数");
        }

        if (StringUtils.isEmpty(orderStoreOrderInfo.getOrderCode())) {
            throw new BusinessException("缺少订单编码");
        }
        OrderStoreOrderInfo order = erpOrderQueryService.getOrderByOrderCode(orderStoreOrderInfo.getOrderCode());
        if (order == null) {
            throw new BusinessException("无效的订单编码");
        }
        if (!ErpOrderStatusEnum.ORDER_STATUS_1.getCode().equals(order.getOrderStatus())) {
            throw new BusinessException("不是" + ErpOrderStatusEnum.ORDER_STATUS_1.getDesc() + "的订单不能发起支付");
        }
        OrderStoreOrderPay orderPay = this.getOrderPayByOrderId(order.getOrderId());
        if (orderPay == null) {
            throw new BusinessException("订单支付信息异常");
        }
        if (!PayStatusEnum.UNPAID.getCode().equals(orderPay.getPayStatus())) {
            throw new BusinessException("非" + PayStatusEnum.UNPAID.getDesc() + "的订单不能发起支付");
        }

        //TODO 调用支付中心接口
//        Map<String, Object> paramMap = new HashMap<>();
//        HttpClient httpClient = HttpClient.post(urlProperties.getPaymentApi() + "/test").json(paramMap);
//        HttpResponse<Object> response = httpClient.action().result(new TypeReference<HttpResponse<Object>>() {
//        });

        //返回状态
        boolean flag = true;
        if (!flag) {
            throw new BusinessException("发起支付失败");
        }

        //更新支付信息
        orderPay.setPayStartTime(new Date());
        orderPay.setPayStatus(PayStatusEnum.PAYING.getCode());
        this.updateOrderPaySelective(orderPay, auth);

        //更新订单状态
        order.setPayStatus(orderPay.getPayStatus());
        erpOrderOperationService.updateOrderByPrimaryKeySelective(order, "发起支付", auth);
    }

    @Override
    public OrderPayResultResponse orderPayResult(OrderStoreOrderInfo orderStoreOrderInfo) {
        OrderPayResultResponse result = new OrderPayResultResponse();
        String orderCode = orderStoreOrderInfo.getOrderCode();
        OrderStoreOrderInfo order = erpOrderQueryService.getOrderByOrderCode(orderCode);
        if (order == null) {
            throw new BusinessException("无效的订单编码");
        }
        result.setOrderCode(order.getOrderCode());
        result.setOrderId(order.getOrderId());
        result.setOrderStatus(order.getOrderStatus());
        result.setPayStatus(order.getPayStatus());
        result.setGoodsCoupon(order.getGoodsCoupon());
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void orderPayPolling(OrderStoreOrderInfo orderStoreOrderInfo) {

        //登录信息
        AuthToken auth = AuthUtil.getCurrentAuth();

        //获取订单
        String orderCode = orderStoreOrderInfo.getOrderCode();
        OrderStoreOrderInfo order = erpOrderQueryService.getOrderByOrderCode(orderCode);
        String orderId = order.getOrderId();
        //获取订单支付信息
        OrderStoreOrderPay orderPay = getOrderPayByOrderId(orderId);

        //轮询请求查询支付信息
        ScheduledExecutorService service = new ScheduledThreadPoolExecutor(1);
        service.scheduleAtFixedRate(new Runnable() {
            int pollingTimes = 0;

            @Override
            public void run() {
                if (pollingTimes == 0) {
                    pollingTimes = orderPay.getPollingTimes();
                }
                logger.info("第" + pollingTimes + "次轮询");
                if (pollingTimes++ > OrderConstant.MAX_PAY_POLLING_TIMES) {
                    //支付超时
                    orderPayEnd(orderId, PayStatusEnum.FAIL, null, auth);
                    service.shutdown();
                }
                OrderStoreOrderPay orderPayByOrderId = getOrderPayByOrderId(orderId);
                if (PayStatusEnum.PAYING.getCode().equals(orderPayByOrderId.getPayStatus())) {
                    PayStatusEnum payStatusEnum = PayStatusEnum.PAYING;
                    String payId = null;
                    try {
                        //TODO 调用支付中心接口 查询支付状态是否成功
                        if (pollingTimes == 4) {
                            payStatusEnum = PayStatusEnum.SUCCESS;
                            payId = OrderPublic.getUUID();

                        }
//                        Map<String, Object> paramMap = new HashMap<>();
//                        HttpClient httpClient = HttpClient.post(urlProperties.getPaymentApi() + "/test").json(paramMap);
//                        HttpResponse<Object> response = httpClient.action().result(new TypeReference<HttpResponse<Object>>() {
//                        });
                    } catch (Exception e) {
                        payStatusEnum = PayStatusEnum.PAYING;
                    }

                    if (PayStatusEnum.SUCCESS == payStatusEnum || PayStatusEnum.FAIL == payStatusEnum) {
                        orderPayEnd(orderId, payStatusEnum, payId, auth);
                    } else {
                        //更新轮询次数
                        OrderStoreOrderPay updateOrderPay = new OrderStoreOrderPay();
                        updateOrderPay.setId(orderPayByOrderId.getId());
                        updateOrderPay.setPollingTimes(orderPayByOrderId.getPollingTimes() == null ? 1 : orderPayByOrderId.getPollingTimes() + 1);
                        updateOrderPaySelective(updateOrderPay, auth);
                    }
                } else {
                    service.shutdown();
                }
            }
            //轮询时间控制
        }, 1000 * 5, 1000 * 20, TimeUnit.MILLISECONDS);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void orderPayCallback(OrderStoreOrderInfo orderStoreOrderInfo) {

        //回调状态
        PayStatusEnum payStatusEnum = PayStatusEnum.SUCCESS;
        //订单号
        String orderCode = orderStoreOrderInfo.getOrderCode();
        String payId = OrderPublic.getUUID();

        AuthToken auth = AuthUtil.getCurrentAuth();
        if (auth.getPersonId() == null || "".equals(auth.getPersonId())) {
            auth = AuthUtil.getSystemAuth();
        }

        OrderStoreOrderInfo order = erpOrderQueryService.getOrderByOrderCode(orderCode);
        if (order == null) {
            throw new BusinessException("无效的订单编号");
        }
        if (!ErpOrderStatusEnum.ORDER_STATUS_1.getCode().equals(order.getOrderStatus())) {
            throw new BusinessException("该订单不是" + ErpOrderStatusEnum.ORDER_STATUS_1.getDesc() + "状态的订单");
        }

        OrderStoreOrderPay orderPay = this.getOrderPayByOrderId(order.getOrderId());
        if (orderPay == null) {
            throw new BusinessException("订单支付信息异常");
        }
        if (!PayStatusEnum.PAYING.getCode().equals(orderPay.getPayStatus())) {
            throw new BusinessException("订单支付信息异常");
        }

        //结束订单支付中状态
        orderPayEnd(order.getOrderId(), payStatusEnum, payId, auth);

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void orderPayRepay(OrderStoreOrderInfo orderStoreOrderInfo) {

        AuthToken auth = AuthUtil.getCurrentAuth();

        if (orderStoreOrderInfo == null || StringUtils.isEmpty(orderStoreOrderInfo.getOrderCode())) {
            throw new BusinessException("空订单编码");
        }
        OrderStoreOrderInfo order = erpOrderQueryService.getOrderByOrderCode(orderStoreOrderInfo.getOrderCode());
        if (order == null) {
            throw new BusinessException("无效的订单编码");
        }
        if (!ErpOrderStatusEnum.ORDER_STATUS_1.getCode().equals(order.getOrderStatus()) && !ErpOrderStatusEnum.ORDER_STATUS_99.getCode().equals(order.getOrderStatus())) {
            throw new BusinessException("只有" + ErpOrderStatusEnum.ORDER_STATUS_1.getDesc() + "和" + ErpOrderStatusEnum.ORDER_STATUS_99.getDesc() + "的订单才支持该操作");
        }

        PayStatusEnum payStatusEnum;
        String payCode = null;
        //TODO 调用支付中心接口 查询支付状态是否成功
        try {
//            Map<String, Object> paramMap = new HashMap<>();
//            HttpClient httpClient = HttpClient.post(urlProperties.getPaymentApi() + "/test").json(paramMap);
//            HttpResponse<Object> response = httpClient.action().result(new TypeReference<HttpResponse<Object>>() {
//            });
            payStatusEnum = PayStatusEnum.SUCCESS;
            payCode = OrderPublic.getUUID();
        } catch (Exception e) {
            throw new BusinessException("查询远程支付状态失败");
        }

        if (payStatusEnum == PayStatusEnum.SUCCESS) {
            //更新支付状态
            OrderStoreOrderPay orderPay = getOrderPayByOrderId(order.getOrderId());
            orderPay.setPayStatus(payStatusEnum.getCode());
            orderPay.setPayEndTime(new Date());
            orderPay.setPayCode(payCode);
            updateOrderPaySelective(orderPay, auth);

            //更新订单状态
            OrderStoreOrderInfo updateOrder = new OrderStoreOrderInfo();
            updateOrder.setId(order.getId());
            updateOrder.setOrderId(order.getOrderId());
            updateOrder.setOrderStatus(ErpOrderStatusEnum.ORDER_STATUS_1.getCode().equals(order.getOrderStatus()) ? ErpOrderStatusEnum.ORDER_STATUS_92.getCode() : ErpOrderStatusEnum.ORDER_STATUS_93.getCode());
            updateOrder.setPayStatus(payStatusEnum.getCode());
            erpOrderOperationService.updateOrderByPrimaryKeySelective(updateOrder, ErpOrderStatusEnum.getEnumDesc(updateOrder.getOrderStatus()), auth);
        } else {
            throw new BusinessException("订单未支付成功");
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void orderTimeoutUnpaid(OrderStoreOrderInfo orderStoreOrderInfo) {

        if (orderStoreOrderInfo == null) {
            throw new BusinessException("空参数");
        }

        if (ErpOrderStatusEnum.ORDER_STATUS_1.getCode().equals(orderStoreOrderInfo.getOrderStatus()) && PayStatusEnum.UNPAID.getCode().equals(orderStoreOrderInfo.getPayStatus())) {
            Date now = new Date();
            Date createTime = orderStoreOrderInfo.getCreateTime();
            if (now.getTime() - createTime.getTime() > OrderConstant.MAX_PAY_TIME_OUT_TIME) {
                AuthToken auth = AuthUtil.getTaskAuth();
                //超时
                orderStoreOrderInfo.setOrderStatus(ErpOrderStatusEnum.ORDER_STATUS_99.getCode());
                orderStoreOrderInfo.setUpdateById(auth.getPersonId());
                orderStoreOrderInfo.setUpdateByName(auth.getPersonName());
                erpOrderOperationService.updateOrderByPrimaryKeySelective(orderStoreOrderInfo, "超时未支付取消订单", auth);
            }
        }
    }

    /**
     * 结束订单支付中状态
     *
     * @param orderId       订单id
     * @param payStatusEnum 支付状态
     * @param payCode       支付中心支付单编号，支付成功才用
     * @param auth          当前用户
     * @return void
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/11/25 17:23
     */
    private void orderPayEnd(String orderId, PayStatusEnum payStatusEnum, String payCode, AuthToken auth) {
        OrderStoreOrderInfo order = erpOrderQueryService.getOrderByOrderId(orderId);
        if (order != null && ErpOrderStatusEnum.ORDER_STATUS_1.getCode().equals(order.getOrderStatus()) && PayStatusEnum.PAYING.getCode().equals(order.getPayStatus())) {
            OrderStoreOrderPay orderPay = this.getOrderPayByOrderId(orderId);
            if (orderPay == null) {
                throw new BusinessException("订单支付信息异常");
            }

            //更新支付信息
            OrderStoreOrderPay updateOrderPay = new OrderStoreOrderPay();
            updateOrderPay.setId(orderPay.getId());
            if (StringUtils.isNotEmpty(payCode)) {
                updateOrderPay.setPayCode(payCode);
            }
            updateOrderPay.setPayEndTime(new Date());
            updateOrderPay.setPayStatus(payStatusEnum.getCode());
            this.updateOrderPaySelective(updateOrderPay, auth);

            //更新订单状态
            OrderStoreOrderInfo updateOrder = new OrderStoreOrderInfo();
            updateOrder.setId(order.getId());
            updateOrder.setOrderId(order.getOrderId());
            updateOrder.setOrderStatus(payStatusEnum == PayStatusEnum.FAIL ? ErpOrderStatusEnum.ORDER_STATUS_99.getCode() : ErpOrderStatusEnum.ORDER_STATUS_2.getCode());
            updateOrder.setPayStatus(payStatusEnum.getCode());
            erpOrderOperationService.updateOrderByPrimaryKeySelective(updateOrder, payStatusEnum == PayStatusEnum.FAIL ? ErpOrderStatusEnum.ORDER_STATUS_99.getDesc() : ErpOrderStatusEnum.ORDER_STATUS_2.getDesc(), auth);
        }

    }

}
