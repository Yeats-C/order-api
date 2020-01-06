package com.aiqin.mgs.order.api.service.bill;


import com.aiqin.mgs.order.api.domain.AuthToken;

public interface OperationLogService {
    void insert(String operationCode, Integer operationType, Integer sourceType, String operationContent, String remark, Integer useStatus, AuthToken auth);
}
