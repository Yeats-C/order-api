package com.aiqin.mgs.order.api.service.impl;

import com.aiqin.mgs.order.api.dao.OrderStoreOrderReceivingDao;
import com.aiqin.mgs.order.api.domain.AuthToken;
import com.aiqin.mgs.order.api.domain.OrderStoreOrderReceiving;
import com.aiqin.mgs.order.api.service.ErpOrderReceivingService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ErpOrderReceivingServiceImpl implements ErpOrderReceivingService {

    @Resource
    private OrderStoreOrderReceivingDao orderStoreOrderReceivingDao;

    @Override
    public OrderStoreOrderReceiving getOrderReceivingByOrderId(String orderId) {
        OrderStoreOrderReceiving orderReceiving = null;
        if (StringUtils.isNotEmpty(orderId)) {
            OrderStoreOrderReceiving orderReceivingQuery = new OrderStoreOrderReceiving();
            orderReceivingQuery.setOrderId(orderId);
            List<OrderStoreOrderReceiving> select = orderStoreOrderReceivingDao.select(orderReceivingQuery);
            if (select != null && select.size() > 0) {
                orderReceiving = select.get(0);
            }
        }
        return orderReceiving;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrderReceiving(OrderStoreOrderReceiving orderReceiving, AuthToken auth) {
        orderReceiving.setCreateById(auth.getPersonId());
        orderReceiving.setCreateByName(auth.getPersonName());
        orderReceiving.setUpdateById(auth.getPersonId());
        orderReceiving.setUpdateByName(auth.getPersonName());
        Integer insert = orderStoreOrderReceivingDao.insert(orderReceiving);
    }

}
