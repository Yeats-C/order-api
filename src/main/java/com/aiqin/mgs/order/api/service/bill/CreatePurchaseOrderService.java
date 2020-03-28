package com.aiqin.mgs.order.api.service.bill;

import com.aiqin.mgs.order.api.domain.po.order.ErpOrderInfo;

import java.util.List;

/**
 * 采购单 接口
 */
public interface CreatePurchaseOrderService {

    /**
     * 根据ERP订单生成爱亲采购单
     */
    void addOrderAndDetail(List<ErpOrderInfo> erpOrderInfo);
}
