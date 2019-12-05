package com.aiqin.mgs.order.api.service.impl;

import com.aiqin.mgs.order.api.dao.OrderStoreOrderSendingDao;
import com.aiqin.mgs.order.api.domain.AuthToken;
import com.aiqin.mgs.order.api.domain.OrderStoreOrderSending;
import com.aiqin.mgs.order.api.service.ErpOrderSendingService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ErpOrderSendingServiceImpl implements ErpOrderSendingService {

    @Resource
    private OrderStoreOrderSendingDao orderStoreOrderSendingDao;


    @Override
    public OrderStoreOrderSending getOrderSendingByOrderId(String orderId) {
        OrderStoreOrderSending orderSending = null;
        if (StringUtils.isNotEmpty(orderId)) {
            OrderStoreOrderSending orderSendingQuery = new OrderStoreOrderSending();
            orderSendingQuery.setOrderId(orderId);
            List<OrderStoreOrderSending> select = orderStoreOrderSendingDao.select(orderSendingQuery);
            if (select != null && select.size() > 0) {
                orderSending = select.get(0);
            }
        }
        return orderSending;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrderSending(OrderStoreOrderSending orderSending, AuthToken auth) {
        orderSending.setCreateById(auth.getPersonId());
        orderSending.setCreateByName(auth.getPersonName());
        orderSending.setUpdateById(auth.getPersonId());
        orderSending.setUpdateByName(auth.getPersonName());
        Integer insert = orderStoreOrderSendingDao.insert(orderSending);
    }

    @Override
    public void updateOrderSendingSelective(OrderStoreOrderSending orderSending, AuthToken auth) {
        orderSending.setUpdateById(auth.getPersonId());
        orderSending.setUpdateByName(auth.getPersonName());
        Integer integer = orderStoreOrderSendingDao.updateByPrimaryKeySelective(orderSending);
    }
}
