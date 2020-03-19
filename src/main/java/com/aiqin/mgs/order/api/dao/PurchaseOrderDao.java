package com.aiqin.mgs.order.api.dao;

import com.aiqin.mgs.order.api.domain.PurchaseOrderInfo;
import com.aiqin.mgs.order.api.domain.request.purchase.PurchaseApplyRequest;
import com.aiqin.mgs.order.api.domain.response.purchase.*;

import java.util.List;

public interface PurchaseOrderDao {
    int deleteByPrimaryKey(Long id);

    int insert(PurchaseOrderInfo record);

    //根据ERP订单生成爱亲采购单
    int insertSelective(PurchaseOrderInfo record);

    PurchaseOrderInfo selectByPrimaryKey(Long id);

    //根据采购单号查询采购单
    PurchaseOrderInfo selectByOrderStoreCode(String orderStoreCode);

    int updateByOrderCode(PurchaseOrderInfo record);

    int updateByPrimaryKey(PurchaseOrderInfo record);

    //取消订单
    int updateByPurchaseOrderStatus(PurchaseOrderInfo record);

    List<PurchaseOrder> purchaseOrderList(PurchaseApplyRequest purchaseApplyRequest);

    Integer purchaseOrderCount(PurchaseApplyRequest purchaseApplyRequest);

    PurchaseOrderInfo purchaseOrderInfo(String purchaseOrderId);
}