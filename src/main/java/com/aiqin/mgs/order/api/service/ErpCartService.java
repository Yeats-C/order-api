package com.aiqin.mgs.order.api.service;

import com.aiqin.mgs.order.api.base.PageResData;
import com.aiqin.mgs.order.api.domain.OrderStoreCart;

import java.util.List;

/**
 * 购物车
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/11/14 18:02
 */
public interface ErpCartService {

    /**
     * 查询购物车列表
     * 后续可能修改为附带商品价格、数量、优惠汇总以及赠品行
     *
     * @param orderStoreCart
     * @return java.util.List<com.aiqin.mgs.order.api.domain.OrderStoreCart>
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/11/16 14:29
     */
    List<OrderStoreCart> findCartProductList(OrderStoreCart orderStoreCart);

    /**
     * 添加商品到购物车
     *
     * @param orderStoreCart
     * @return void
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/11/16 14:29
     */
    void addCartProduct(OrderStoreCart orderStoreCart);

    /**
     * 从购物车删除商品
     *
     * @param orderStoreCart
     * @return void
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/11/16 14:30
     */
    void deleteCartProduct(OrderStoreCart orderStoreCart);
}
