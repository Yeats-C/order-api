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
    public OrderStoreOrderSending getOrderSendingBySendingId(String sendingId) {
        OrderStoreOrderSending orderSending = null;
        if (StringUtils.isNotEmpty(sendingId)) {
            OrderStoreOrderSending orderSendingQuery = new OrderStoreOrderSending();
            orderSendingQuery.setSendingId(sendingId);
            List<OrderStoreOrderSending> select = orderStoreOrderSendingDao.select(orderSendingQuery);
            if (select != null && select.size() > 0) {
                orderSending = select.get(0);
            }
        }
        return orderSending;
    }

    @Override
    public OrderStoreOrderSending getOrderSendingByLogisticCode(String logisticCode) {
        OrderStoreOrderSending orderSending = null;
        if (StringUtils.isNotEmpty(logisticCode)) {
            OrderStoreOrderSending orderSendingQuery = new OrderStoreOrderSending();
            orderSendingQuery.setLogisticCode(logisticCode);
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
