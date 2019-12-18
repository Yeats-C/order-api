package com.aiqin.mgs.order.api.service.bill;

import com.aiqin.mgs.order.api.domain.request.bill.PurchaseOrderReq;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


public class PurchaseOrderServiceTest {
    @Autowired
    PurchaseOrderService purchaseOrderService;

    @Test
    public void testCreateSaleOrder(){
        PurchaseOrderReq purchaseOrderReq = new PurchaseOrderReq();
        //purchaseOrderReq.setOrderId(String.valueOf(new Date()));
        System.out.println(purchaseOrderService.createSaleOrder(purchaseOrderReq));
    }
}