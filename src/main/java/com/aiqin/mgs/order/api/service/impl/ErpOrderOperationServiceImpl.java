package com.aiqin.mgs.order.api.service.impl;

import com.aiqin.mgs.order.api.dao.OrderStoreOrderInfoDao;
import com.aiqin.mgs.order.api.domain.AuthToken;
import com.aiqin.mgs.order.api.domain.OrderStoreOrderInfo;
import com.aiqin.mgs.order.api.service.ErpOrderOperationLogService;
import com.aiqin.mgs.order.api.service.ErpOrderOperationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
@Transactional(rollbackFor = Exception.class)
public class ErpOrderOperationServiceImpl implements ErpOrderOperationService {

    @Resource
    private OrderStoreOrderInfoDao orderStoreOrderInfoDao;

    @Resource
    private ErpOrderOperationLogService erpOrderOperationLogService;

    @Override
    public void saveOrder(OrderStoreOrderInfo orderInfo, AuthToken auth) {
        //TODO 数据校验

        //保存订单
        orderInfo.setCreateById(auth.getPersonId());
        orderInfo.setCreateByName(auth.getPersonName());
        orderInfo.setUpdateById(auth.getPersonId());
        orderInfo.setUpdateByName(auth.getPersonName());
        Integer insert = orderStoreOrderInfoDao.insert(orderInfo);

        //保存订单操作日志
        erpOrderOperationLogService.saveOrderOperationLog(orderInfo.getOrderId(), "创建订单", auth);
    }

    @Override
    public void updateOrderByPrimaryKeySelective(OrderStoreOrderInfo orderInfo, String operationContent, AuthToken auth) {
        //TODO 数据校验

        //更新订单数据
        orderInfo.setUpdateById(auth.getPersonId());
        orderInfo.setUpdateByName(auth.getPersonName());
        Integer integer = orderStoreOrderInfoDao.updateByPrimaryKeySelective(orderInfo);

        //保存订单操作日志
        erpOrderOperationLogService.saveOrderOperationLog(orderInfo.getOrderId(), operationContent, auth);

    }
}
