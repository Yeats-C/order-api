package com.aiqin.mgs.order.api.service.bill;

import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.domain.DeliveryInfoVo;
import com.aiqin.mgs.order.api.domain.PurchaseInfoVo;
import com.aiqin.mgs.order.api.domain.po.order.ErpOrderInfo;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 爱亲采购单 接口
 */
@Service
public interface PurchaseOrderService {
    /**
     * 同步采购单
     * @param erpOrderInfo
     * @return
     */
    HttpResponse createPurchaseOrder (ErpOrderInfo erpOrderInfo);

    /**
     * 耘链销售单回传
     * @param purchaseInfoVo
     * @return
     */
    HttpResponse updatePurchaseInfo(List<PurchaseInfoVo> purchaseInfoVo);

    /**
     * 发运单回传
     * @param deliveryInfoVo
     * @return
     */
    HttpResponse updateOrderStoreLogistics(List<DeliveryInfoVo> deliveryInfoVo);
}
