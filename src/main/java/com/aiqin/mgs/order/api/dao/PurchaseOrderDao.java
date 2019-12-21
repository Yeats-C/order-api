package com.aiqin.mgs.order.api.dao;

import com.aiqin.mgs.order.api.domain.PurchaseOrder;

public interface PurchaseOrderDao {
    int deleteByPrimaryKey(Long id);

    int insert(PurchaseOrder record);

    int insertSelective(PurchaseOrder record);

    PurchaseOrder selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PurchaseOrder record);

    int updateByPrimaryKey(PurchaseOrder record);
}