package com.aiqin.mgs.order.api.service.impl;

import com.aiqin.mgs.order.api.component.enums.ErpOrderStatusEnum;
import com.aiqin.mgs.order.api.component.enums.OrderLevelEnum;
import com.aiqin.mgs.order.api.dao.OrderStoreOrderInfoDao;
import com.aiqin.mgs.order.api.domain.AuthToken;
import com.aiqin.mgs.order.api.domain.OrderStoreOrderInfo;
import com.aiqin.mgs.order.api.service.ErpOrderOperationLogService;
import com.aiqin.mgs.order.api.service.ErpOrderOperationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
public class ErpOrderOperationServiceImpl implements ErpOrderOperationService {

    @Resource
    private OrderStoreOrderInfoDao orderStoreOrderInfoDao;

    @Resource
    private ErpOrderOperationLogService erpOrderOperationLogService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrder(OrderStoreOrderInfo orderInfo, AuthToken auth) {

        //保存订单
        orderInfo.setCreateById(auth.getPersonId());
        orderInfo.setCreateByName(auth.getPersonName());
        orderInfo.setUpdateById(auth.getPersonId());
        orderInfo.setUpdateByName(auth.getPersonName());
        Integer insert = orderStoreOrderInfoDao.insert(orderInfo);

        //保存订单操作日志
        erpOrderOperationLogService.saveOrderOperationLog(orderInfo.getOrderId(), OrderLevelEnum.PRIMARY.getCode().equals(orderInfo.getOrderLevel()) ? ErpOrderStatusEnum.ORDER_STATUS_1 : ErpOrderStatusEnum.ORDER_STATUS_3, auth);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateOrderByPrimaryKeySelective(OrderStoreOrderInfo orderInfo, AuthToken auth) {

        //更新订单数据
        orderInfo.setUpdateById(auth.getPersonId());
        orderInfo.setUpdateByName(auth.getPersonName());
        Integer integer = orderStoreOrderInfoDao.updateByPrimaryKeySelective(orderInfo);

        //保存订单操作日志
        erpOrderOperationLogService.saveOrderOperationLog(orderInfo.getOrderId(), ErpOrderStatusEnum.getEnum(orderInfo.getOrderStatus()), auth);
    }
}
