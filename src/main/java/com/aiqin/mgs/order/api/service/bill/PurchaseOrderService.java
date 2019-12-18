package com.aiqin.mgs.order.api.service.bill;

import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.domain.request.bill.PurchaseOrderReq;

/**
 * 销售 接口
 */
public interface PurchaseOrderService {
    /**
     * 创建销售
     * @param purchaseOrderReq
     * @return
     */
    HttpResponse createSaleOrder (PurchaseOrderReq purchaseOrderReq);
}
