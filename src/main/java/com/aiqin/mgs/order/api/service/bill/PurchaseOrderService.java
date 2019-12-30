package com.aiqin.mgs.order.api.service.bill;

import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.domain.PurchaseInfo;
import com.aiqin.mgs.order.api.domain.po.order.ErpOrderInfo;

import java.util.List;

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

    /**
     * 耘链销售单回传
     * @return
     */
    HttpResponse<List<PurchaseInfo>> selectPurchaseInfo();
}
