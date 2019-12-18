package com.aiqin.mgs.order.api.dao;

import com.aiqin.mgs.order.api.domain.PurchaseOrderDetailBatch;

public interface PurchaseOrderDetailBatchDao {
    int deleteByPrimaryKey(Long id);

    int insert(PurchaseOrderDetailBatch record);

    int insertSelective(PurchaseOrderDetailBatch record);

    PurchaseOrderDetailBatch selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PurchaseOrderDetailBatch record);

    int updateByPrimaryKey(PurchaseOrderDetailBatch record);
}