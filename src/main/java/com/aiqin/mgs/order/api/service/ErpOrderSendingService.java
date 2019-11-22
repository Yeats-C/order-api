package com.aiqin.mgs.order.api.service;

import com.aiqin.mgs.order.api.domain.OrderStoreOrderSending;

/**
 * 订单发货信息service
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/11/21 17:48
 */
public interface ErpOrderSendingService {

    /**
     * 根据订单id获取发货信息
     *
     * @param orderId 订单id
     * @return com.aiqin.mgs.order.api.domain.OrderStoreOrderSending
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/11/21 17:48
     */
    OrderStoreOrderSending getOrderSendingByOrderId(String orderId);

    /**
     * 保存订单发货信息
     *
     * @param orderStoreOrderSending
     * @return void
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/11/21 17:49
     */
    void saveOrderSending(OrderStoreOrderSending orderStoreOrderSending);
}
