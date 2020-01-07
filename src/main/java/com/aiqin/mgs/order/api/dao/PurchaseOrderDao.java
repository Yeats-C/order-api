package com.aiqin.mgs.order.api.dao;

import com.aiqin.mgs.order.api.domain.PurchaseOrder;

public interface PurchaseOrderDao {
    int deleteByPrimaryKey(Long id);

    int insert(PurchaseOrder record);

    /**
     * 根据ERP订单生成爱亲采购单
     * @param record
     * @return
     */
    int insertSelective(PurchaseOrder record);

    PurchaseOrder selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PurchaseOrder record);

    int updateByPrimaryKey(PurchaseOrder record);
    /**
     * 取消订单
     * @param record
     * @return
     */
    int updateByorderStoreCode(PurchaseOrder record);
}