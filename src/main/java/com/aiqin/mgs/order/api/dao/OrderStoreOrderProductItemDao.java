package com.aiqin.mgs.order.api.dao;

import com.aiqin.mgs.order.api.domain.OrderStoreOrderProductItem;

import java.util.List;

/**
 * 订单商品明细
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/11/14 15:58
 */
public interface OrderStoreOrderProductItemDao {

    /**
     * 插入订单商品行数据
     *
     * @param orderStoreOrderProductItem
     * @return java.lang.Integer
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/11/20 11:08
     */
    Integer insert(OrderStoreOrderProductItem orderStoreOrderProductItem);

    /**
     * 根据非空字段精确查询
     *
     * @param orderStoreOrderProductItem
     * @return java.util.List<com.aiqin.mgs.order.api.domain.OrderStoreOrderProductItem>
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/11/22 9:32
     */
    List<OrderStoreOrderProductItem> select(OrderStoreOrderProductItem orderStoreOrderProductItem);
}
