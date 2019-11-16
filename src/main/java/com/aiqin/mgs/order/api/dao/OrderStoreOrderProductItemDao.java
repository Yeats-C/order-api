package com.aiqin.mgs.order.api.dao;

import com.aiqin.mgs.order.api.domain.OrderStoreOrderProductItem;
import org.apache.ibatis.annotations.Param;

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
     * 根据订单id查询订单商品明细列表
     *
     * @param orderId
     * @return java.util.List<com.aiqin.mgs.order.api.domain.OrderStoreOrderProductItem>
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/11/14 15:58
     */
    List<OrderStoreOrderProductItem> selectOrderProductListByOrderId(@Param("orderId") String orderId);
}
