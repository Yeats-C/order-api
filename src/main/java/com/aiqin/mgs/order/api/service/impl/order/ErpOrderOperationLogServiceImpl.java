package com.aiqin.mgs.order.api.service.impl.order;

import com.aiqin.mgs.order.api.component.enums.ErpOrderStatusEnum;
import com.aiqin.mgs.order.api.dao.order.ErpOrderOperationLogDao;
import com.aiqin.mgs.order.api.domain.AuthToken;
import com.aiqin.mgs.order.api.domain.po.order.ErpOrderOperationLog;
import com.aiqin.mgs.order.api.service.order.ErpOrderOperationLogService;
import com.aiqin.mgs.order.api.util.OrderPublic;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class ErpOrderOperationLogServiceImpl implements ErpOrderOperationLogService {

    @Resource
    private ErpOrderOperationLogDao erpOrderOperationLogDao;

    @Override
    public List<ErpOrderOperationLog> selectOrderOperationLogListByOrderId(String orderId) {
        return erpOrderOperationLogDao.selectOperationLogListByOrderId(orderId);
    }

    @Override
    public void saveOrderOperationLog(String orderId, ErpOrderStatusEnum orderStatusEnum, AuthToken auth) {
        if (orderStatusEnum != null) {
            ErpOrderOperationLog operationLog = new ErpOrderOperationLog();
            operationLog.setOrderId(orderId);
            operationLog.setOrderStatus(orderStatusEnum.getCode());
            operationLog.setOperationContent(orderStatusEnum.getDesc());
            operationLog.setLogId(OrderPublic.getUUID());
            operationLog.setCreateTime(new Date());
            operationLog.setCreateById(auth.getPersonId());
            operationLog.setCreateByName(auth.getPersonName());
            Integer insert = erpOrderOperationLogDao.insert(operationLog);
        }
    }

    @Override
    public void copySplitOrderLog(String orderId, List<ErpOrderOperationLog> list) {
        for (ErpOrderOperationLog item :
                list) {
            item.setOrderId(orderId);
            Integer insert = erpOrderOperationLogDao.insert(item);
        }
    }
}
