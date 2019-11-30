package com.aiqin.mgs.order.api.service;

import com.aiqin.mgs.order.api.base.PageResData;
import com.aiqin.mgs.order.api.domain.OrderStoreOrderInfo;
import com.aiqin.mgs.order.api.domain.request.ErpOrderSaveRequest;
import com.aiqin.mgs.order.api.domain.response.ErpOrderDetailResponse;

/**
 * erp订单相关
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/11/12 15:18
 */
public interface ErpOrderService {

    /**
     * 查询erp订单列表
     *
     * @param orderStoreOrderInfo
     * @return com.aiqin.mgs.order.api.base.PageResData<com.aiqin.mgs.order.api.domain.OrderStoreOrderInfo>
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/11/12 15:52
     */
    PageResData<OrderStoreOrderInfo> findOrderList(OrderStoreOrderInfo orderStoreOrderInfo);

    /**
     * 查询订单详情
     *
     * @param orderStoreOrderInfo
     * @return com.aiqin.mgs.order.api.domain.response.ErpOrderDetailResponse
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/11/13 16:33
     */
    ErpOrderDetailResponse getOrderDetail(OrderStoreOrderInfo orderStoreOrderInfo);

    /**
     * 创建配送订单
     *
     * @param erpOrderSaveRequest
     * @return com.aiqin.mgs.order.api.domain.OrderStoreOrderInfo
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/11/13 10:15
     */
    OrderStoreOrderInfo saveDistributionOrder(ErpOrderSaveRequest erpOrderSaveRequest);

    /**
     * 创建货架订单
     *
     * @param erpOrderSaveRequest
     * @return com.aiqin.mgs.order.api.domain.OrderStoreOrderInfo
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/11/27 14:06
     */
    OrderStoreOrderInfo saveRackOrder(ErpOrderSaveRequest erpOrderSaveRequest);

    /**
     * 订单拆分
     *
     * @param orderStoreOrderInfo
     * @return void
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/11/28 10:01
     */
    void orderSplit(OrderStoreOrderInfo orderStoreOrderInfo);
}
