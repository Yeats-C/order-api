package com.aiqin.mgs.order.api.dao;

import com.aiqin.mgs.order.api.domain.PurchaseOrder;

public interface PurchaseOrderDao {
    int deleteByPrimaryKey(Long id);

    int insert(PurchaseOrder record);

    //根据ERP订单生成爱亲采购单
    int insertSelective(PurchaseOrder record);

    PurchaseOrder selectByPrimaryKey(Long id);

    //根据采购单号查询采购单
    PurchaseOrder selectByOrderStoreCode(String orderStoreCode);

    int updateByOrderCode(PurchaseOrder record);

    int updateByPrimaryKey(PurchaseOrder record);

    //取消订单
    int updateByPurchaseOrderStatus(PurchaseOrder record);
}