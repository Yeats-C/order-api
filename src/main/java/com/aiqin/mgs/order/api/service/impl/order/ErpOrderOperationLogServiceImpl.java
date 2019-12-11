package com.aiqin.mgs.order.api.service.impl.order;

import com.aiqin.mgs.order.api.component.enums.ErpOrderStatusEnum;
import com.aiqin.mgs.order.api.dao.order.ErpOrderOperationLogDao;
import com.aiqin.mgs.order.api.domain.AuthToken;
import com.aiqin.mgs.order.api.domain.po.order.ErpOrderOperationLog;
import com.aiqin.mgs.order.api.service.order.ErpOrderOperationLogService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ErpOrderOperationLogServiceImpl implements ErpOrderOperationLogService {

    @Resource
    private ErpOrderOperationLogDao erpOrderOperationLogDao;

    @Override
    public List<ErpOrderOperationLog> selectOrderOperationLogListByOrderId(String orderId) {
        List<ErpOrderOperationLog> list = new ArrayList<>();
        if (StringUtils.isNotEmpty(orderId)) {
            ErpOrderOperationLog query = new ErpOrderOperationLog();
            query.setOrderId(orderId);
            list = erpOrderOperationLogDao.select(query);
        }
        return list;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrderOperationLog(String orderId, ErpOrderStatusEnum orderStatusEnum, AuthToken auth) {
        if (orderStatusEnum != null) {
            ErpOrderOperationLog operationLog = new ErpOrderOperationLog();
            operationLog.setOrderId(orderId);
            operationLog.setOrderStatus(orderStatusEnum.getCode());
            operationLog.setOperationContent(orderStatusEnum.getDesc());
            operationLog.setCreateTime(new Date());
            operationLog.setCreateById(auth.getPersonId());
            operationLog.setCreateByName(auth.getPersonName());
            Integer insert = erpOrderOperationLogDao.insert(operationLog);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void copySplitOrderLog(String orderId, List<ErpOrderOperationLog> list) {
        for (ErpOrderOperationLog item :
                list) {
            item.setOrderId(orderId);
            Integer insert = erpOrderOperationLogDao.insert(item);
        }
    }
}
