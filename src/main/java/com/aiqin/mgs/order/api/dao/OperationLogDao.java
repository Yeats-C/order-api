package com.aiqin.mgs.order.api.dao;

import com.aiqin.mgs.order.api.domain.OperationLog;
import com.aiqin.mgs.order.api.domain.response.OrderOperationLog;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OperationLogDao {
    int deleteByPrimaryKey(Long id);

    /**
     * 添加操作日志
     * @param record
     * @return
     */
    int insert(OperationLog record);

    int insertSelective(OperationLog record);

    OperationLog selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OperationLog record);

    int updateByPrimaryKey(OperationLog record);

    List<OrderOperationLog> list(String operationId);

    List<OperationLog> searchOrderLog(@Param("operationCode")String operationCode, @Param("sourceType") Integer sourceType);
}