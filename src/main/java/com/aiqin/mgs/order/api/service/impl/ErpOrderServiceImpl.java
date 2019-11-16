package com.aiqin.mgs.order.api.service.impl;

import com.aiqin.mgs.order.api.base.PageResData;
import com.aiqin.mgs.order.api.base.exception.BusinessException;
import com.aiqin.mgs.order.api.dao.*;
import com.aiqin.mgs.order.api.domain.*;
import com.aiqin.mgs.order.api.domain.response.ErpOrderDetailResponse;
import com.aiqin.mgs.order.api.service.ErpOrderService;
import com.aiqin.mgs.order.api.util.PageAutoHelperUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ErpOrderServiceImpl implements ErpOrderService {

    @Resource
    private OrderStoreOrderInfoDao orderStoreOrderInfoDao;

    @Resource
    private OrderStoreOrderProductItemDao orderStoreOrderProductItemDao;

    @Resource
    private OrderStoreOrderPayDao orderStoreOrderPayDao;

    @Resource
    private OrderStoreOrderReceivingDao orderStoreOrderReceivingDao;

    @Resource
    private OrderStoreOrderSendingDao orderStoreOrderSendingDao;

    @Resource
    private OrderStoreOrderOperationLogDao orderStoreOrderOperationLogDao;

    @Override
    public PageResData<OrderStoreOrderInfo> findOrderList(OrderStoreOrderInfo orderStoreOrderInfo) {
        return PageAutoHelperUtil.generatePageRes(() -> orderStoreOrderInfoDao.findOrderList(orderStoreOrderInfo), orderStoreOrderInfo);
    }

    @Override
    public ErpOrderDetailResponse getOrderDetail(OrderStoreOrderInfo orderStoreOrderInfo) {
        ErpOrderDetailResponse response = new ErpOrderDetailResponse();

        if (orderStoreOrderInfo == null) {
            throw new BusinessException("空参数");
        }
        if (StringUtils.isEmpty(orderStoreOrderInfo.getOrderId()) && StringUtils.isEmpty(orderStoreOrderInfo.getOrderCode())) {
            throw new BusinessException("请传入订单id或订单编码");
        }

        //查询订单
        List<OrderStoreOrderInfo> orderList = orderStoreOrderInfoDao.select(orderStoreOrderInfo);
        if (orderList == null || orderList.size() <= 0) {
            throw new BusinessException("订单不存在");
        }
        OrderStoreOrderInfo order = orderList.get(0);
        response.setOrderInfo(order);

        //查询订单商品行
        List<OrderStoreOrderProductItem> orderProductItemList = orderStoreOrderProductItemDao.selectOrderProductListByOrderId(order.getOrderId());
        response.setOrderProductItemList(orderProductItemList);

        //查询订单支付信息
        OrderStoreOrderPay orderPay = orderStoreOrderPayDao.getOrderPayByOrderId(order.getOrderId());
        response.setOrderPay(orderPay);

        //查询收货信息
        OrderStoreOrderReceiving orderReceiving = orderStoreOrderReceivingDao.getOrderReceivingByOrderId(order.getOrderId());
        response.setOrderReceiving(orderReceiving);

        //查询订单发货信息
        OrderStoreOrderSending orderSending = orderStoreOrderSendingDao.getOrderSendingByOrderId(order.getOrderId());
        response.setOrderSending(orderSending);

        //查询订单操作日志
        List<OrderStoreOrderOperationLog> orderOperationLogList = orderStoreOrderOperationLogDao.selectOperationLogListByOrderId(order.getOrderId());
        response.setOrderOperationLogList(orderOperationLogList);

        return response;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrder(OrderStoreOrderInfo orderStoreOrderInfo) {

    }

}
