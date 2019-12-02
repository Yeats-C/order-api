package com.aiqin.mgs.order.api.service;

import com.aiqin.mgs.order.api.domain.AuthToken;
import com.aiqin.mgs.order.api.domain.OrderStoreOrderProductItem;

import java.util.List;

/**
 * 订单明细行service
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/11/21 18:00
 */
public interface ErpOrderProductItemService {

    /**
     * 根据订单id查询订单商品明细列表
     *
     * @param orderId
     * @return java.util.List<com.aiqin.mgs.order.api.domain.OrderStoreOrderProductItem>
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/11/14 15:58
     */
    List<OrderStoreOrderProductItem> selectOrderProductListByOrderId(String orderId);

    /**
     * 保存订单商品行
     *
     * @param orderStoreOrderProductItem
     * @param auth
     * @return void
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/11/22 9:38
     */
    void saveOrderProductItem(OrderStoreOrderProductItem orderStoreOrderProductItem, AuthToken auth);

    /**
     * 批量保存订单商品行
     *
     * @param list
     * @param auth
     * @return void
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/11/22 9:39
     */
    void saveOrderProductItemList(List<OrderStoreOrderProductItem> list, AuthToken auth);

    /**
     * 更新订单商品行
     *
     * @param orderStoreOrderProductItem
     * @param auth
     * @return void
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/11/22 9:38
     */
    void updateOrderProductItem(OrderStoreOrderProductItem orderStoreOrderProductItem, AuthToken auth);

    /**
     * 批量更新订单商品行
     *
     * @param list
     * @param auth
     * @return void
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/11/22 9:39
     */
    void updateOrderProductItemList(List<OrderStoreOrderProductItem> list, AuthToken auth);

}
