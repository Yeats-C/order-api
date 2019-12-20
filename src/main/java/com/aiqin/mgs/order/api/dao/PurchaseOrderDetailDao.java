package com.aiqin.mgs.order.api.dao;

import com.aiqin.mgs.order.api.domain.PurchaseOrderDetail;

import java.util.List;

public interface PurchaseOrderDetailDao {
    int deleteByPrimaryKey(Long id);

    int insertList(List<PurchaseOrderDetail> record);

    int insertSelective(PurchaseOrderDetail record);

    PurchaseOrderDetail selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PurchaseOrderDetail record);

    int updateByPrimaryKey(PurchaseOrderDetail record);
}