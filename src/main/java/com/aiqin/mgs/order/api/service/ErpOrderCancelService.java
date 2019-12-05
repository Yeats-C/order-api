package com.aiqin.mgs.order.api.service;

import com.aiqin.mgs.order.api.domain.OrderStoreOrderInfo;

/**
 * 订单取消
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/12/5 10:05
 */
public interface ErpOrderCancelService {

    /**
     * 缺货终止交易
     *
     * @param orderStoreOrderInfo
     * @return void
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/5 10:00
     */
    void cancelOrderWithoutStock(OrderStoreOrderInfo orderStoreOrderInfo);

    /**
     * 拒收终止交易
     *
     * @param orderStoreOrderInfo
     * @return void
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/5 10:02
     */
    void cancelOrderRejectSign(OrderStoreOrderInfo orderStoreOrderInfo);

    /**
     * 申请取消订单
     *
     * @param orderStoreOrderInfo
     * @return void
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/5 10:02
     */
    void applyCancelOrder(OrderStoreOrderInfo orderStoreOrderInfo);
    
}
