package com.aiqin.mgs.order.api.service;

import com.aiqin.mgs.order.api.domain.AuthToken;
import com.aiqin.mgs.order.api.domain.OrderStoreOrderReceiving;

/**
 * 订单发货信息service
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/11/21 16:45
 */
public interface ErpOrderReceivingService {

    /**
     * 根据订单id查询订单收货信息
     *
     * @param orderId 订单id
     * @return com.aiqin.mgs.order.api.domain.OrderStoreOrderReceiving
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/11/21 16:46
     */
    OrderStoreOrderReceiving getOrderReceivingByOrderId(String orderId);

    /**
     * 保存订单收货信息
     *
     * @param orderReceiving 订单收货信息
     * @param auth           操作人
     * @return void
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/11/21 16:53
     */
    void saveOrderReceiving(OrderStoreOrderReceiving orderReceiving, AuthToken auth);
}
