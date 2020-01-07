package com.aiqin.mgs.order.api.service.bill;


import com.aiqin.mgs.order.api.domain.AuthToken;
/**
 * 爱亲供应链，操作日志 实现类
 */
public interface OperationLogService {
    /**
     * 添加日志
     * @param operationCode
     * @param operationType
     * @param sourceType
     * @param operationContent
     * @param remark
     * @param useStatus
     * @param auth
     */
    void insert(String operationCode, Integer operationType, Integer sourceType, String operationContent, String remark, Integer useStatus, AuthToken auth);
}
