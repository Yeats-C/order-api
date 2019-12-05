package com.aiqin.mgs.order.api.service;

import com.aiqin.mgs.order.api.domain.AuthToken;
import com.aiqin.mgs.order.api.domain.OrderStoreOrderInfo;
import com.aiqin.mgs.order.api.domain.OrderStoreOrderPay;
import com.aiqin.mgs.order.api.domain.response.order.OrderPayResultResponse;

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
     * @param auth
     * @return void
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/11/20 15:25
     */
    void saveOrderPay(OrderStoreOrderPay orderStoreOrderPay, AuthToken auth);

    /**
     * 根据主键更新非空字段
     *
     * @param orderStoreOrderPay
     * @param auth
     * @return void
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/11/20 16:21
     */
    void updateOrderPaySelective(OrderStoreOrderPay orderStoreOrderPay,AuthToken auth);

    /**
     * 发起支付
     *
     * @param orderStoreOrderPay
     * @return void
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/11/20 15:26
     */
    void orderPay(OrderStoreOrderPay orderStoreOrderPay);

    /**
     * 查询订单支付结果
     *
     * @param orderStoreOrderPay
     * @return com.aiqin.mgs.order.api.domain.response.order.OrderPayResultResponse
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/11/25 14:55
     */
    OrderPayResultResponse orderPayResult(OrderStoreOrderPay orderStoreOrderPay);

    /**
     * 轮询订单支付状态
     *
     * @param orderStoreOrderPay
     * @return void
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/11/20 16:38
     */
    void orderPayPolling(OrderStoreOrderPay orderStoreOrderPay);

    /**
     * 订单支付回调方法
     *
     * @param orderStoreOrderPay
     * @return void
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/11/20 15:26
     */
    void orderPayCallback(OrderStoreOrderPay orderStoreOrderPay);

    /**
     * 校验并更正订单支付信息
     *
     * @param orderStoreOrderPay
     * @return void
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/11/25 17:59
     */
    void orderPayRepay(OrderStoreOrderPay orderStoreOrderPay);

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
