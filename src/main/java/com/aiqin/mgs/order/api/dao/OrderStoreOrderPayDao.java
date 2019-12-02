package com.aiqin.mgs.order.api.dao;

import com.aiqin.mgs.order.api.domain.OrderStoreOrderPay;

import java.util.List;

/**
 * 订单支付信息
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/11/14 11:18
 */
public interface OrderStoreOrderPayDao {

    /**
     * 根据非空条件精确筛选
     *
     * @param orderStoreOrderPay
     * @return java.util.List<com.aiqin.mgs.order.api.domain.OrderStoreOrderPay>
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/11/26 15:54
     */
    List<OrderStoreOrderPay> select(OrderStoreOrderPay orderStoreOrderPay);

    /**
     * 插入订单支付信息数据
     *
     * @param orderStoreOrderPay
     * @return java.lang.Integer
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/11/20 11:09
     */
    Integer insert(OrderStoreOrderPay orderStoreOrderPay);

    /**
     * 根据主键更新非空字段
     *
     * @param orderStoreOrderPay
     * @return java.lang.Integer
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/11/20 11:09
     */
    Integer updateByPrimaryKeySelective(OrderStoreOrderPay orderStoreOrderPay);
}
