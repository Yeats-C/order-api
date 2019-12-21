package com.aiqin.mgs.order.api.service.impl.order;

import com.aiqin.mgs.order.api.component.enums.ErpLogOperationTypeEnum;
import com.aiqin.mgs.order.api.component.enums.ErpLogSourceTypeEnum;
import com.aiqin.mgs.order.api.component.enums.StatusEnum;
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
    public void saveOrderOperationLog(String operationCode, ErpLogSourceTypeEnum sourceTypeEnum, ErpLogOperationTypeEnum operationTypeEnum, String operationContent, String remark, AuthToken auth) {
        ErpOrderOperationLog operationLog = new ErpOrderOperationLog();
        Date now = new Date();

        operationLog.setOperationCode(operationCode);
        operationLog.setOperationType(operationTypeEnum.getCode());
        operationLog.setSourceType(sourceTypeEnum.getCode());
        operationLog.setOperationContent(operationContent);
        operationLog.setRemark(remark);

        operationLog.setUseStatus(StatusEnum.YES.getCode());
        operationLog.setCreateTime(now);
        operationLog.setCreateById(auth.getPersonId());
        operationLog.setCreateByName(auth.getPersonName());
        operationLog.setUpdateTime(now);
        operationLog.setUpdateById(auth.getPersonId());
        operationLog.setUpdateByName(auth.getPersonName());
        Integer insert = erpOrderOperationLogDao.insert(operationLog);
    }

    @Override
    public List<ErpOrderOperationLog> selectOrderOperationLogList(String orderCode) {
        List<ErpOrderOperationLog> list = new ArrayList<>();
        if (StringUtils.isNotEmpty(orderCode)) {
            ErpOrderOperationLog query = new ErpOrderOperationLog();
            query.setOperationCode(orderCode);
            query.setSourceType(ErpLogSourceTypeEnum.SALES.getCode());
            List<ErpOrderOperationLog> select = erpOrderOperationLogDao.select(query);
            if (select != null && select.size() > 0) {
                for (ErpOrderOperationLog item :
                        select) {
                    if (!ErpLogOperationTypeEnum.DOWNLOAD.getCode().equals(item.getOperationType())) {
                        list.add(item);
                    }
                }
            }
        }
        return list;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void copySplitOrderLog(String orderCode, List<ErpOrderOperationLog> list) {
        for (ErpOrderOperationLog item :
                list) {
            item.setOperationCode(orderCode);
            Integer insert = erpOrderOperationLogDao.insert(item);
        }
    }

}
