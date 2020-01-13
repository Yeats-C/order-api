package com.aiqin.mgs.order.api.service.bill.impl;

import com.aiqin.mgs.order.api.dao.OperationLogDao;
import com.aiqin.mgs.order.api.domain.AuthToken;
import com.aiqin.mgs.order.api.domain.OperationLog;
import com.aiqin.mgs.order.api.service.bill.OperationLogService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 爱亲供应链，操作日志 实现类
 */
@Service
public class OperationLogServiceImpl implements OperationLogService {
    @Resource
    OperationLogDao operationLogDao;

    @Override
    public void insert(String operationCode, Integer operationType, Integer sourceType, String operationContent, String remark, Integer useStatus, AuthToken auth) {
        OperationLog operationLog = new OperationLog();
        operationLog.setOperationCode(operationCode);//来源编码
        operationLog.setOperationType(operationType);//日志类型 0 .新增 1.修改 2.删除 3.下载
        operationLog.setSourceType(sourceType);//来源类型 0.销售 1.采购 2.退货  3.退供
        operationLog.setOperationContent(operationContent);//日志内容
        operationLog.setRemark(remark);//备注
        operationLog.setUseStatus(String.valueOf(useStatus));//0. 启用 1.禁用
        //operationLog.setCreateById(auth.getPersonId());
        //operationLog.setCreateByName(auth.getPersonName());
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        operationLog.setCreateTime(formatter.format(new Date()));
        //添加日志
        operationLogDao.insert(operationLog);
    }
}
