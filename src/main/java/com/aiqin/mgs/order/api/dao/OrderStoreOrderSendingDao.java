package com.aiqin.mgs.order.api.dao;

import com.aiqin.mgs.order.api.domain.OrderStoreOrderSending;

import java.util.List;

/**
 * 订单发货信息
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/11/14 11:41
 */
public interface OrderStoreOrderSendingDao {

    /**
     * 非空字段精确筛选
     *
     * @param orderStoreOrderSending
     * @return java.util.List<com.aiqin.mgs.order.api.domain.OrderStoreOrderSending>
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/11/21 17:52
     */
    List<OrderStoreOrderSending> select(OrderStoreOrderSending orderStoreOrderSending);

    /**
     * 插入数据
     *
     * @param orderStoreOrderSending
     * @return java.lang.Integer
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/11/21 17:46
     */
    Integer insert(OrderStoreOrderSending orderStoreOrderSending);

}
