package com.aiqin.mgs.order.api.service.impl;

import com.aiqin.mgs.order.api.dao.OrderStoreOrderInfoDao;
import com.aiqin.mgs.order.api.domain.OrderStoreOrderInfo;
import com.aiqin.mgs.order.api.service.ErpOrderQueryService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ErpOrderQueryServiceImpl implements ErpOrderQueryService {

    @Resource
    private OrderStoreOrderInfoDao orderStoreOrderInfoDao;

    @Override
    public OrderStoreOrderInfo getOrderByOrderId(String orderId) {
        OrderStoreOrderInfo orderInfo = null;
        if (StringUtils.isNotEmpty(orderId)) {
            OrderStoreOrderInfo orderInfoQuery = new OrderStoreOrderInfo();
            orderInfoQuery.setOrderId(orderId);
            List<OrderStoreOrderInfo> select = orderStoreOrderInfoDao.select(orderInfoQuery);
            if (select != null && select.size() > 0) {
                orderInfo = select.get(0);
            }
        }
        return orderInfo;
    }

    @Override
    public OrderStoreOrderInfo getOrderByOrderCode(String orderCode) {
        OrderStoreOrderInfo orderInfo = null;
        if (StringUtils.isNotEmpty(orderCode)) {
            OrderStoreOrderInfo orderInfoQuery = new OrderStoreOrderInfo();
            orderInfoQuery.setOrderCode(orderCode);
            List<OrderStoreOrderInfo> select = orderStoreOrderInfoDao.select(orderInfoQuery);
            if (select != null && select.size() > 0) {
                orderInfo = select.get(0);
            }
        }
        return orderInfo;
    }

    @Override
    public List<OrderStoreOrderInfo> selectOrderBySelective(OrderStoreOrderInfo orderInfo) {
        return orderStoreOrderInfoDao.select(orderInfo);
    }
}
