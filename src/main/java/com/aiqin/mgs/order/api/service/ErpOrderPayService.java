package com.aiqin.mgs.order.api.service;

import com.aiqin.mgs.order.api.domain.OrderStoreOrderInfo;
import com.aiqin.mgs.order.api.domain.OrderStoreOrderPay;

/**
 * 订单支付service
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/11/20 15:26
 */
public interface ErpOrderPayService {

    /**
     * 根据订单id查询订单支付信息
     *
     * @param orderId
     * @return com.aiqin.mgs.order.api.domain.OrderStoreOrderPay
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/11/20 15:25
     */
    OrderStoreOrderPay getOrderPayByOrderId(String orderId);

    /**
     * 保存订单支付信息
     *
     * @param orderStoreOrderPay
     * @return void
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/11/20 15:25
     */
    void saveOrderPay(OrderStoreOrderPay orderStoreOrderPay);

    /**
     * 根据主键更新非空字段
     *
     * @param orderStoreOrderPay
     * @return void
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/11/20 16:21
     */
    void updateOrderPaySelective(OrderStoreOrderPay orderStoreOrderPay);

    /**
     * 发起支付
     *
     * @param orderStoreOrderInfo
     * @return void
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/11/20 15:26
     */
    void orderPay(OrderStoreOrderInfo orderStoreOrderInfo);

    /**
     * 轮询订单支付状态
     *
     * @param orderStoreOrderInfo
     * @return void
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/11/20 16:38
     */
    void orderPayPolling(OrderStoreOrderInfo orderStoreOrderInfo);

    /**
     * 订单支付回调方法
     *
     * @param orderStoreOrderInfo
     * @return void
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/11/20 15:26
     */
    void orderPayCallback(OrderStoreOrderInfo orderStoreOrderInfo);

    /**
     * 订单超时未支付取消订单
     *
     * @param orderStoreOrderInfo
     * @return void
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/11/21 9:57
     */
    void orderTimeoutUnpaid(OrderStoreOrderInfo orderStoreOrderInfo);
}
