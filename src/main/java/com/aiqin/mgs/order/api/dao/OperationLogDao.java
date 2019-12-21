package com.aiqin.mgs.order.api.dao;

import com.aiqin.mgs.order.api.domain.OperationLog;

public interface OperationLogDao {
    int deleteByPrimaryKey(Long id);

    int insert(OperationLog record);

    int insertSelective(OperationLog record);

    OperationLog selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OperationLog record);

    int updateByPrimaryKey(OperationLog record);
}