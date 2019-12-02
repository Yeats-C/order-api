package com.aiqin.mgs.order.api.service.impl;

import com.aiqin.mgs.order.api.component.enums.ErpOrderStatusEnum;
import com.aiqin.mgs.order.api.dao.OrderStoreOrderOperationLogDao;
import com.aiqin.mgs.order.api.domain.AuthToken;
import com.aiqin.mgs.order.api.domain.OrderStoreOrderOperationLog;
import com.aiqin.mgs.order.api.service.ErpOrderOperationLogService;
import com.aiqin.mgs.order.api.util.OrderPublic;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional(rollbackFor = Exception.class)
    public void saveOrderOperationLog(String orderId, ErpOrderStatusEnum orderStatusEnum, AuthToken auth) {
        if (orderStatusEnum != null) {
            OrderStoreOrderOperationLog operationLog = new OrderStoreOrderOperationLog();
            operationLog.setOrderId(orderId);
            operationLog.setOrderStatus(orderStatusEnum.getCode());
            operationLog.setOperationContent(orderStatusEnum.getDesc());
            operationLog.setLogId(OrderPublic.getUUID());
            operationLog.setCreateById(auth.getPersonId());
            operationLog.setCreateByName(auth.getPersonName());
            Integer insert = orderStoreOrderOperationLogDao.insert(operationLog);
        }
    }
}
