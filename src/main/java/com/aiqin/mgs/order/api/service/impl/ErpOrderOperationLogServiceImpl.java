package com.aiqin.mgs.order.api.service.impl;

import com.aiqin.mgs.order.api.dao.OrderStoreOrderOperationLogDao;
import com.aiqin.mgs.order.api.domain.AuthToken;
import com.aiqin.mgs.order.api.domain.OrderStoreOrderOperationLog;
import com.aiqin.mgs.order.api.service.ErpOrderOperationLogService;
import com.aiqin.mgs.order.api.util.OrderPublic;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ErpOrderOperationLogServiceImpl implements ErpOrderOperationLogService {

    @Resource
    private OrderStoreOrderOperationLogDao orderStoreOrderOperationLogDao;

    @Override
    public List<OrderStoreOrderOperationLog> selectOperationLogListByOrderId(String orderId) {
        return orderStoreOrderOperationLogDao.selectOperationLogListByOrderId(orderId);
    }

    @Override
    public void saveOrderOperationLog(String orderId, String operationContent, AuthToken auth) {
        OrderStoreOrderOperationLog operationLog = new OrderStoreOrderOperationLog();
        operationLog.setOrderId(orderId);
        operationLog.setOperationContent(operationContent);
        operationLog.setLogId(OrderPublic.getUUID());
        operationLog.setCreateById(auth.getPersonId());
        operationLog.setCreateByName(auth.getPersonName());
        Integer insert = orderStoreOrderOperationLogDao.insert(operationLog);
    }
}
