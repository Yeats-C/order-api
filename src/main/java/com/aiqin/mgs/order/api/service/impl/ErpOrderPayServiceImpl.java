package com.aiqin.mgs.order.api.service.impl;

import com.aiqin.ground.util.http.HttpClient;
import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.base.exception.BusinessException;
import com.aiqin.mgs.order.api.component.enums.ErpOrderStatusEnum;
import com.aiqin.mgs.order.api.component.enums.PayStatusEnum;
import com.aiqin.mgs.order.api.config.properties.UrlProperties;
import com.aiqin.mgs.order.api.dao.OrderStoreOrderInfoDao;
import com.aiqin.mgs.order.api.dao.OrderStoreOrderPayDao;
import com.aiqin.mgs.order.api.domain.AuthToken;
import com.aiqin.mgs.order.api.domain.OrderStoreOrderInfo;
import com.aiqin.mgs.order.api.domain.OrderStoreOrderPay;
import com.aiqin.mgs.order.api.service.ErpOrderOperationLogService;
import com.aiqin.mgs.order.api.service.ErpOrderPayService;
import com.aiqin.mgs.order.api.util.AuthUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Service
public class ErpOrderPayServiceImpl implements ErpOrderPayService {

    @Resource
    private OrderStoreOrderInfoDao orderStoreOrderInfoDao;

    @Resource
    private OrderStoreOrderPayDao orderStoreOrderPayDao;

    @Resource
    private ErpOrderOperationLogService erpOrderOperationLogService;

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
        OrderStoreOrderInfo order = orderStoreOrderInfoDao.selectByOrderCode(orderStoreOrderInfo.getOrderCode());
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
        orderStoreOrderPayDao.updateByPrimaryKeySelective(orderPay);

        //更新订单状态
        order.setPayStatus(orderPay.getPayStatus());
        order.setUpdateById(auth.getPersonId());
        order.setUpdateByName(auth.getPersonName());
        orderStoreOrderInfoDao.updateByPrimaryKeySelective(order);

        //保存订单日志
        erpOrderOperationLogService.saveOrderOperationLog(order.getOrderId(), "发起支付");

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

        OrderStoreOrderInfo order = orderStoreOrderInfoDao.selectByOrderCode(orderCode);
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
        orderStoreOrderPayDao.updateByPrimaryKeySelective(orderPay);

        //更新订单状态
        order.setOrderStatus(ErpOrderStatusEnum.ORDER_STATUS_2.getCode());
        order.setPayStatus(PayStatusEnum.SUCCESS.getCode());
        order.setUpdateById(auth.getPersonId());
        order.setUpdateByName(auth.getPersonName());
        orderStoreOrderInfoDao.updateByPrimaryKeySelective(order);

        //保存订单日志
        erpOrderOperationLogService.saveOrderOperationLog(order.getOrderId(), "支付成功");
    }

}
