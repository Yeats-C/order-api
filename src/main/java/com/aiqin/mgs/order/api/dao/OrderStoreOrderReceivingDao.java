package com.aiqin.mgs.order.api.dao;

import com.aiqin.mgs.order.api.domain.OrderStoreOrderReceiving;

import java.util.List;

/**
 * 订单收货信息
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/11/14 11:31
 */
public interface OrderStoreOrderReceivingDao {

    /**
     * 非空字段精确筛选
     *
     * @param orderStoreOrderReceiving
     * @return java.util.List<com.aiqin.mgs.order.api.domain.OrderStoreOrderReceiving>
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/11/21 17:09
     */
    List<OrderStoreOrderReceiving> select(OrderStoreOrderReceiving orderStoreOrderReceiving);

    /**
     * 保存数据
     *
     * @param orderStoreOrderReceiving
     * @return java.lang.Integer
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/11/21 17:09
     */
    Integer insert(OrderStoreOrderReceiving orderStoreOrderReceiving);
}
