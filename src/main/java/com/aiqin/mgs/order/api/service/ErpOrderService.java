package com.aiqin.mgs.order.api.service;

import com.aiqin.mgs.order.api.base.PageResData;
import com.aiqin.mgs.order.api.domain.OrderStoreOrderInfo;
import com.aiqin.mgs.order.api.domain.request.ErpOrderSaveRequest;

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
     * @return com.aiqin.mgs.order.api.domain.OrderStoreOrderInfo
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/5 15:50
     */
    OrderStoreOrderInfo getOrderDetail(OrderStoreOrderInfo orderStoreOrderInfo);

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

    /**
     * 订单发货
     *
     * @param orderStoreOrderInfo
     * @return void
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/3 19:35
     */
    void orderSend(OrderStoreOrderInfo orderStoreOrderInfo);

    /**
     * 查询订单物流信息
     *
     * @param orderStoreOrderInfo
     * @return com.aiqin.mgs.order.api.domain.OrderStoreOrderInfo
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/4 15:16
     */
    OrderStoreOrderInfo getOrderSendingFee(OrderStoreOrderInfo orderStoreOrderInfo);

    /**
     * 查询订单签收信息
     *
     * @param orderStoreOrderInfo
     * @return com.aiqin.mgs.order.api.domain.OrderStoreOrderInfo
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/11/30 15:25
     */
    OrderStoreOrderInfo getOrderSignDetail(OrderStoreOrderInfo orderStoreOrderInfo);

    /**
     * 订单签收操作
     *
     * @param orderStoreOrderInfo
     * @return void
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/11/30 15:25
     */
    void orderSign(OrderStoreOrderInfo orderStoreOrderInfo);
}
