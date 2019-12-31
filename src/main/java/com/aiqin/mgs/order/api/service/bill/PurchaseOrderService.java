package com.aiqin.mgs.order.api.service.bill;

import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.domain.po.order.ErpOrderInfo;

/**
 * 销售 接口
 */
public interface PurchaseOrderService {
    /**
     * 同步采购单
     * @param erpOrderInfo
     * @return
     */
    HttpResponse createPurchaseOrder (ErpOrderInfo erpOrderInfo);
}
