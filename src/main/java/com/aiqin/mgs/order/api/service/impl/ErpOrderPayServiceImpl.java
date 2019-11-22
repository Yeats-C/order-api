package com.aiqin.mgs.order.api.service.impl;

import com.aiqin.ground.util.http.HttpClient;
import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.base.exception.BusinessException;
import com.aiqin.mgs.order.api.component.enums.ErpOrderStatusEnum;
import com.aiqin.mgs.order.api.component.enums.PayStatusEnum;
import com.aiqin.mgs.order.api.config.properties.UrlProperties;
import com.aiqin.mgs.order.api.dao.OrderStoreOrderPayDao;
import com.aiqin.mgs.order.api.domain.AuthToken;
import com.aiqin.mgs.order.api.domain.OrderStoreOrderInfo;
import com.aiqin.mgs.order.api.domain.OrderStoreOrderPay;
import com.aiqin.mgs.order.api.domain.constant.OrderConstant;
import com.aiqin.mgs.order.api.service.ErpOrderOperationService;
import com.aiqin.mgs.order.api.service.ErpOrderPayService;
import com.aiqin.mgs.order.api.service.ErpOrderQueryService;
import com.aiqin.mgs.order.api.util.AuthUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

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
        if (StringUtils.isEmpty(orderId)) {
            throw new BusinessException("无效的订单id");
        }
        return orderStoreOrderPayDao.getOrderPayByOrderId(orderId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrderPay(OrderStoreOrderPay orderStoreOrderPay) {
        Integer insert = orderStoreOrderPayDao.insert(orderStoreOrderPay);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateOrderPaySelective(OrderStoreOrderPay orderStoreOrderPay) {
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
        if (PayStatusEnum.UNPAID.getCode().equals(orderPay.getPayStatus())) {
            throw new BusinessException("非" + PayStatusEnum.UNPAID.getDesc() + "的订单不能发起支付");
        }

        //TODO 调用支付中心接口
        Map<String, Object> paramMap = new HashMap<>();
        HttpClient httpClient = HttpClient.post(urlProperties.getPaymentApi() + "/test").json(paramMap);
        HttpResponse<Object> response = httpClient.action().result(new TypeReference<HttpResponse<Object>>() {
        });

        //返回状态
        boolean flag = true;
        if (!flag) {
            throw new BusinessException("发起支付失败");
        }

        //更新支付信息
        orderPay.setPayStatus(PayStatusEnum.PAYING.getCode());
        orderPay.setUpdateById(auth.getPersonId());
        orderPay.setUpdateByName(auth.getPersonName());
        this.updateOrderPaySelective(orderPay);

        //更新订单状态
        order.setPayStatus(orderPay.getPayStatus());
        erpOrderOperationService.updateOrderByPrimaryKeySelective(order, "发起支付", auth);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void orderPayPolling(OrderStoreOrderInfo orderStoreOrderInfo) {

        AuthToken auth = AuthUtil.getTaskSystemAuth();

        if (orderStoreOrderInfo == null) {
            throw new BusinessException("空参数");
        }

        OrderStoreOrderPay orderPay = this.getOrderPayByOrderId(orderStoreOrderInfo.getOrderId());

        //订单支付状态
        PayStatusEnum payStatusEnum;
        //轮询次数小于最大轮询次数
        Integer pollingTimes = orderPay.getPollingTimes() == null ? 0 : orderPay.getPollingTimes();

        if (pollingTimes <= OrderConstant.MAX_PAY_POLLING_TIMES) {
            try {
                //轮询次数加1
                pollingTimes++;
                orderPay.setPollingTimes(pollingTimes);

                //TODO 调用支付中心接口 查询支付状态是否成功
                if (1 == 1) {
                    throw new BusinessException("没有接口");
                }
                Map<String, Object> paramMap = new HashMap<>();
                HttpClient httpClient = HttpClient.post(urlProperties.getPaymentApi() + "/test").json(paramMap);
                HttpResponse<Object> response = httpClient.action().result(new TypeReference<HttpResponse<Object>>() {
                });

                // 1支付成功
                payStatusEnum = PayStatusEnum.SUCCESS;
                // 2支付失败
                // payStatusEnum = PayStatusEnum.FAIL;
                // 3支付未完成
                // payStatusEnum = PayStatusEnum.PAYING;

            } catch (Exception e) {
                //请求失败
                payStatusEnum = PayStatusEnum.PAYING;
                logger.info("请求支付中心查询订单支付状态失败，订单号：{}，异常信息：{}", orderStoreOrderInfo.getOrderCode(), e);
            }
        } else {
            //找过最大轮询次数，支付失败
            payStatusEnum = PayStatusEnum.FAIL;
        }

        if (payStatusEnum != PayStatusEnum.PAYING) {
            //支付完成 包括支付成功和支付失败
            orderStoreOrderInfo.setOrderStatus(payStatusEnum == PayStatusEnum.SUCCESS ? ErpOrderStatusEnum.ORDER_STATUS_2.getCode() : ErpOrderStatusEnum.ORDER_STATUS_99.getCode());
            orderStoreOrderInfo.setPayStatus(payStatusEnum.getCode());
            erpOrderOperationService.updateOrderByPrimaryKeySelective(orderStoreOrderInfo, ErpOrderStatusEnum.getEnumDesc(orderStoreOrderInfo.getOrderStatus()), auth);
        }

        //更新订单支付信息
        orderPay.setPayStatus(payStatusEnum.getCode());
        orderPay.setUpdateById(auth.getPersonId());
        orderPay.setUpdateByName(auth.getPersonName());
        this.updateOrderPaySelective(orderPay);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void orderPayCallback(OrderStoreOrderInfo orderStoreOrderInfo) {

        //回调状态
        boolean flag = true;
        //订单号
        String orderCode = "";

        AuthToken auth = AuthUtil.getCurrentAuth();
        if (auth.getPersonId() == null || "".equals(auth.getPersonId())) {
            throw new BusinessException("请先登录");
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

        //更新订单支付信息
        orderPay.setPayStatus(PayStatusEnum.SUCCESS.getCode());
        orderPay.setUpdateById(auth.getPersonId());
        orderPay.setUpdateByName(auth.getPersonName());
        this.updateOrderPaySelective(orderPay);

        //更新订单状态
        order.setOrderStatus(ErpOrderStatusEnum.ORDER_STATUS_2.getCode());
        order.setPayStatus(PayStatusEnum.SUCCESS.getCode());
        erpOrderOperationService.updateOrderByPrimaryKeySelective(order, "支付成功", auth);

        //TODO 还有订单支付失败

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
                AuthToken auth = AuthUtil.getTaskSystemAuth();
                //超时
                orderStoreOrderInfo.setOrderStatus(ErpOrderStatusEnum.ORDER_STATUS_99.getCode());
                orderStoreOrderInfo.setUpdateById(auth.getPersonId());
                orderStoreOrderInfo.setUpdateByName(auth.getPersonName());
                erpOrderOperationService.updateOrderByPrimaryKeySelective(orderStoreOrderInfo,"超时未支付取消订单",auth);
            }
        }
    }

}
