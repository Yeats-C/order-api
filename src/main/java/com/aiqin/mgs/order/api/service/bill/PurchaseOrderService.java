package com.aiqin.mgs.order.api.service.bill;

import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.domain.DeliveryInfoVo;
import com.aiqin.mgs.order.api.domain.OrderIogisticsVo;
import com.aiqin.mgs.order.api.domain.po.order.ErpOrderInfo;

/**
 * 爱亲采购单 接口
 */

public interface PurchaseOrderService {
    /**
     * 同步采购单
     *
     * @param erpOrderInfo
     * @return
     */
    HttpResponse createPurchaseOrder(ErpOrderInfo erpOrderInfo);

    /**
     * 耘链销售单回传
     *
     * @param orderIogisticsVo
     * @return
     */
    HttpResponse updatePurchaseInfo(OrderIogisticsVo orderIogisticsVo);

    /**
     * 发运单回传
     *
     * @param deliveryInfoVo
     * @return
     */
    HttpResponse updateOrderStoreLogistics(DeliveryInfoVo deliveryInfoVo);

    /**
     * 取消订单
     * @param orderStoreCode
     * @return
     */
    HttpResponse updateCancelOrderinfo(String orderStoreCode);
}
