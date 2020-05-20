package com.aiqin.mgs.order.api.dao;

import com.aiqin.mgs.order.api.domain.PurchaseBatch;
import com.aiqin.mgs.order.api.domain.request.purchase.PurchaseOrderProductRequest;

import java.util.List;

public interface PurchaseBatchDao {

    int insertSelective(PurchaseBatch record);

    List<PurchaseBatch> purchaseOrderBatchList(PurchaseOrderProductRequest request);

    Integer purchaseOrderBatchCount(PurchaseOrderProductRequest request);
}