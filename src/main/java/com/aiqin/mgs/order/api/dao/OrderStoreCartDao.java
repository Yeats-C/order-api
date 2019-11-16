package com.aiqin.mgs.order.api.dao;

import com.aiqin.mgs.order.api.domain.OrderStoreCart;

import java.util.List;

/**
 * 购物车
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/11/14 18:04
 */
public interface OrderStoreCartDao {

    /**
     * 查询购物车列表，条件自定义
     *
     * @param orderStoreCart
     * @return java.util.List<com.aiqin.mgs.order.api.domain.OrderStoreCart>
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/11/16 14:19
     */
    List<OrderStoreCart> findCartProductList(OrderStoreCart orderStoreCart);

    /**
     * 插入数据
     *
     * @param orderStoreCart
     * @return java.lang.Integer
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/11/16 14:19
     */
    Integer insert(OrderStoreCart orderStoreCart);

    /**
     * 根据cartId删除数据
     *
     * @param cartId
     * @return java.lang.Integer
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/11/16 14:19
     */
    Integer deleteByCartId(String cartId);
}
