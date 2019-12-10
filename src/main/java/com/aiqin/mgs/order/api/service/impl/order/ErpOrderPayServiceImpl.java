package com.aiqin.mgs.order.api.service.impl.order;

import com.aiqin.mgs.order.api.base.exception.BusinessException;
import com.aiqin.mgs.order.api.component.enums.*;
import com.aiqin.mgs.order.api.dao.order.ErpOrderPayDao;
import com.aiqin.mgs.order.api.domain.AuthToken;
import com.aiqin.mgs.order.api.domain.constant.OrderConstant;
import com.aiqin.mgs.order.api.domain.po.order.ErpOrderInfo;
import com.aiqin.mgs.order.api.domain.po.order.ErpOrderLogistics;
import com.aiqin.mgs.order.api.domain.po.order.ErpOrderPay;
import com.aiqin.mgs.order.api.domain.request.order.ErpOrderPayCallbackRequest;
import com.aiqin.mgs.order.api.domain.request.order.ErpOrderPayRequest;
import com.aiqin.mgs.order.api.domain.response.order.ErpOrderPayStatusResponse;
import com.aiqin.mgs.order.api.domain.response.order.OrderPayResultResponse;
import com.aiqin.mgs.order.api.service.order.*;
import com.aiqin.mgs.order.api.util.AuthUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Service
public class ErpOrderPayServiceImpl implements ErpOrderPayService {

    private static final Logger logger = LoggerFactory.getLogger(ErpOrderPayServiceImpl.class);

    @Resource
    private ErpOrderPayDao erpOrderPayDao;
    @Resource
    private ErpOrderQueryService erpOrderQueryService;
    @Resource
    private ErpOrderInfoService erpOrderInfoService;
    @Resource
    private ErpOrderItemService erpOrderItemService;
    @Resource
    private ErpOrderRequestService erpOrderRequestService;
    @Resource
    private ErpOrderLogisticsService erpOrderLogisticsService;

    @Override
    public ErpOrderPay getOrderPayByPayId(String payId) {
        ErpOrderPay orderPay = null;
        if (StringUtils.isNotEmpty(payId)) {
            ErpOrderPay query = new ErpOrderPay();
            query.setPayId(payId);
            List<ErpOrderPay> select = erpOrderPayDao.select(query);
            if (select != null && select.size() > 0) {
                orderPay = select.get(0);
            }
        }
        return orderPay;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrderPay(ErpOrderPay po, AuthToken auth) {
        po.setCreateById(auth.getPersonId());
        po.setCreateByName(auth.getPersonName());
        po.setUpdateById(auth.getPersonId());
        po.setUpdateByName(auth.getPersonName());
        Integer insert = erpOrderPayDao.insert(po);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateOrderPaySelective(ErpOrderPay po, AuthToken auth) {
        po.setUpdateById(auth.getPersonId());
        po.setUpdateByName(auth.getPersonName());
        Integer integer = erpOrderPayDao.updateByPrimaryKeySelective(po);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String orderPay(ErpOrderPayRequest erpOrderPayRequest) {

        AuthToken auth = AuthUtil.getCurrentAuth();

        if (erpOrderPayRequest == null) {
            throw new BusinessException("空参数");
        }
        if (StringUtils.isEmpty(erpOrderPayRequest.getOrderCode())) {
            throw new BusinessException("缺少订单编码");
        }
        if (erpOrderPayRequest.getPayWay() == null) {
            throw new BusinessException("缺少支付方式");
        } else {
            if (!ErpPayWayEnum.exist(erpOrderPayRequest.getPayWay())) {
                throw new BusinessException("无效的支付方式");
            }
        }
        ErpOrderInfo order = erpOrderQueryService.getOrderByOrderCode(erpOrderPayRequest.getOrderCode());
        if (order == null) {
            throw new BusinessException("无效的订单编码");
        }
        if (!ErpOrderStatusEnum.ORDER_STATUS_1.getCode().equals(order.getOrderStatus())) {
            throw new BusinessException("不是" + ErpOrderStatusEnum.ORDER_STATUS_1.getDesc() + "的订单不能发起支付");
        }

        ErpOrderPay orderPay = this.getOrderPayByPayId(order.getPayId());
        if (orderPay == null) {
            throw new BusinessException("订单支付信息异常");
        }
        if (!ErpPayStatusEnum.UNPAID.getCode().equals(orderPay.getPayStatus())) {
            throw new BusinessException("非" + ErpPayStatusEnum.UNPAID.getDesc() + "的订单不能发起支付");
        }

        //调用支付中心接口
        boolean flag = erpOrderRequestService.sendPayRequest(order, orderPay);

        if (!flag) {
            throw new BusinessException("发起支付失败");
        }

        //更新支付信息
        orderPay.setPayStartTime(new Date());
        orderPay.setPayStatus(ErpPayStatusEnum.PAYING.getCode());
        orderPay.setPayWay(erpOrderPayRequest.getPayWay());
        this.updateOrderPaySelective(orderPay, auth);

        return orderPay.getPayId();
    }

    @Override
    public OrderPayResultResponse orderPayResult(ErpOrderPayRequest erpOrderPayRequest) {
        OrderPayResultResponse result = new OrderPayResultResponse();
        String orderCode = erpOrderPayRequest.getOrderCode();
        ErpOrderInfo order = erpOrderQueryService.getOrderByOrderCode(orderCode);
        if (order == null) {
            throw new BusinessException("无效的订单编码");
        }
        ErpOrderPay orderPay = this.getOrderPayByPayId(order.getPayId());
        result.setOrderCode(order.getOrderCode());
        result.setOrderId(order.getOrderId());
        result.setOrderStatus(order.getOrderStatus());
        result.setGoodsCoupon(order.getGoodsCoupon());
        result.setPayStatus(orderPay.getPayStatus());
        result.setPayCode(orderPay.getPayCode());
        result.setPayStartTime(orderPay.getPayStartTime());
        result.setPayEndTime(orderPay.getPayEndTime());
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void payPolling(String payId) {

        //登录信息
        AuthToken auth = AuthUtil.getCurrentAuth();

        //轮询请求查询支付信息
        ScheduledExecutorService service = new ScheduledThreadPoolExecutor(1);
        service.scheduleAtFixedRate(new Runnable() {

            int pollingTimes = 0;

            @Override
            public void run() {
                ErpOrderPay orderPay = getOrderPayByPayId(payId);
                if (orderPay == null) {
                    throw new BusinessException("无效的支付id");
                }
                if (!ErpPayStatusEnum.PAYING.getCode().equals(orderPay.getPayStatus())) {
                    service.shutdown();
                }
                pollingTimes++;
                logger.info("第{}次轮询", pollingTimes);
                if (pollingTimes > OrderConstant.MAX_PAY_POLLING_TIMES) {
                    //支付超时
                    orderPay.setPayStatus(ErpPayStatusEnum.FAIL.getCode());
                    orderPay.setPayEndTime(new Date());
                    updateOrderPaySelective(orderPay, auth);
                    endPay(payId);
                    service.shutdown();
                }
                ErpOrderPayStatusResponse payStatusResponse = erpOrderRequestService.getOrderPayStatus(payId);
                if (payStatusResponse.isRequestSuccess()) {
                    if (ErpPayStatusEnum.FAIL == payStatusResponse.getPayStatusEnum() || ErpPayStatusEnum.SUCCESS == payStatusResponse.getPayStatusEnum()) {

                        orderPay.setPayStatus(payStatusResponse.getPayStatusEnum().getCode());
                        orderPay.setPayEndTime(new Date());
                        updateOrderPaySelective(orderPay, auth);
                        endPay(payId);
                        service.shutdown();
                    }
                }
            }
            //轮询时间控制
        }, OrderConstant.MAX_PAY_POLLING_INITIALDELAY, OrderConstant.MAX_PAY_POLLING_PERIOD, TimeUnit.MILLISECONDS);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String orderPayCallback(ErpOrderPayCallbackRequest erpOrderPayCallbackRequest) {

        //TODO CT 校验参数

        //回调状态
        ErpPayStatusEnum payStatusEnum = ErpPayStatusEnum.SUCCESS;
        //订单号
        String payId = erpOrderPayCallbackRequest.getPayId();
        //支付中心支付单号
        String payCode = erpOrderPayCallbackRequest.getPayCode();

        AuthToken auth = AuthUtil.getSystemAuth();
        if (auth.getPersonId() == null || "".equals(auth.getPersonId())) {
            auth = AuthUtil.getSystemAuth();
        }

        ErpOrderPay orderPay = getOrderPayByPayId(payId);
        if (orderPay == null) {
            throw new BusinessException("无效的payId");
        }
        if (ErpPayStatusEnum.UNPAID.getCode().equals(orderPay.getPayStatus())) {
            throw new BusinessException("单据未发起支付");
        }
        if (ErpPayStatusEnum.FAIL.getCode().equals(orderPay.getPayStatus())) {
            throw new BusinessException("单据已经支付失败");
        }
        if (ErpPayStatusEnum.SUCCESS.getCode().equals(orderPay.getPayStatus())) {
            throw new BusinessException("单据已经支付成功");
        }

        orderPay.setPayStatus(payStatusEnum.getCode());
        orderPay.setPayCode(payCode);
        orderPay.setPayEndTime(new Date());
        this.updateOrderPaySelective(orderPay, auth);

        //支付单支付成功后续逻辑
        endPay(payId);
        return payId;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String endPay(String payId) {

        AuthToken auth = AuthUtil.getSystemAuth();
        ErpOrderPay orderPay = this.getOrderPayByPayId(payId);
        if (orderPay == null) {
            throw new BusinessException("无效的payId");
        }
        //支付单类型
        ErpPayFeeTypeEnum payFeeTypeEnum = ErpPayFeeTypeEnum.getEnum(orderPay.getFeeType());
        //支付单状态
        ErpPayStatusEnum payStatusEnum = ErpPayStatusEnum.getEnum(orderPay.getPayStatus());
        switch (payFeeTypeEnum) {
            //订单费用
            case ORDER_FEE:
                ErpOrderInfo order = erpOrderQueryService.getOrderByPayId(payId);
                if (order == null) {
                    throw new BusinessException("数据异常");
                }
                order.setOrderItemList(erpOrderItemService.selectOrderItemListByOrderId(order.getOrderId()));
                if (payStatusEnum == ErpPayStatusEnum.SUCCESS) {
                    order.setOrderStatus(ErpOrderStatusEnum.ORDER_STATUS_2.getCode());
                    order.setPaid(YesOrNoEnum.YES.getCode());
                    //获取物流券
                    BigDecimal goodsCoupon = erpOrderRequestService.getGoodsCoupon(order);
                    order.setGoodsCoupon(goodsCoupon);
                    erpOrderInfoService.updateOrderByPrimaryKeySelective(order, auth);

                    //请求供应链，获取库存分组，进行拆单
                    ScheduledExecutorService splitService = new ScheduledThreadPoolExecutor(1);
                    splitService.scheduleAtFixedRate(new Runnable() {
                        @Override
                        public void run() {

                            //请求供应链解锁库存
                            erpOrderInfoService.orderSplit(order);

                            splitService.shutdown();
                        }
                        //轮询时间控制
                    },0, 1000L, TimeUnit.MILLISECONDS);

                } else if (payStatusEnum == ErpPayStatusEnum.FAIL) {
                    order.setOrderStatus(ErpOrderStatusEnum.ORDER_STATUS_99.getCode());
                    order.setPaid(YesOrNoEnum.NO.getCode());
                    erpOrderInfoService.updateOrderByPrimaryKeySelective(order, auth);

                    //解锁库存
                    ScheduledExecutorService unlockStockService = new ScheduledThreadPoolExecutor(1);
                    unlockStockService.scheduleAtFixedRate(new Runnable() {
                        @Override
                        public void run() {

                            //请求供应链解锁库存
                            erpOrderRequestService.unlockStockInSupplyChain(order);

                            unlockStockService.shutdown();
                        }
                        //轮询时间控制
                    },0, 1000L, TimeUnit.MILLISECONDS);

                } else {

                }
                break;

            //物流费用
            case LOGISTICS_FEE:
                //查询物流费用
                ErpOrderLogistics orderLogistics = erpOrderLogisticsService.getOrderLogisticsByPayId(orderPay.getPayId());
                if (orderLogistics == null) {
                    throw new BusinessException("数据异常");
                }
                if (payStatusEnum == ErpPayStatusEnum.SUCCESS) {
                    orderLogistics.setPaid(YesOrNoEnum.YES.getCode());
                    erpOrderLogisticsService.updateOrderLogisticsSelective(orderLogistics, auth);
                    List<ErpOrderInfo> orderList = erpOrderQueryService.getOrderByLogisticsId(orderLogistics.getLogisticsId());
                    if (orderList != null && orderList.size() > 0) {
                        //订单修改为翼支付物流费用
                        for (ErpOrderInfo item :
                                orderList) {
                            if (ErpOrderStatusEnum.ORDER_STATUS_11.getCode().equals(item.getOrderStatus())) {
                                item.setOrderStatus(ErpOrderStatusEnum.ORDER_STATUS_12.getCode());
                                erpOrderInfoService.updateOrderByPrimaryKeySelective(item, auth);
                            }
                        }
                    }
                }
                ;
                break;
            default:
                ;
        }


        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void orderPayRepay(ErpOrderPayRequest erpOrderPayRequest) {

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void orderTimeoutUnpaid(ErpOrderPayRequest erpOrderPayRequest) {

    }

}
