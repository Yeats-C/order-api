package com.aiqin.mgs.order.api.dao;

import com.aiqin.mgs.order.api.domain.OrderStoreOrderPay;
import org.apache.ibatis.annotations.Param;

/**
 * 订单支付信息
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/11/14 11:18
 */
public interface OrderStoreOrderPayDao {

    /**
     * 根据订单id查询订单支付信息
     *
     * @param orderId
     * @return com.aiqin.mgs.order.api.domain.OrderStoreOrderPay
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/11/14 15:53
     */
    OrderStoreOrderPay getOrderPayByOrderId(@Param("orderId") String orderId);
}
