package com.aiqin.mgs.order.api.dao;

import com.aiqin.mgs.order.api.domain.ReturnOrderDetailBatch;

import java.util.List;

public interface ReturnOrderDetailBatchDao {
    int deleteByPrimaryKey(Long id);

    int insert(ReturnOrderDetailBatch record);

    int insertSelective(ReturnOrderDetailBatch record);

    ReturnOrderDetailBatch selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ReturnOrderDetailBatch record);

    int updateByPrimaryKey(ReturnOrderDetailBatch record);

    List<ReturnOrderDetailBatch> select(ReturnOrderDetailBatch returnOrderDetailBatch);
}