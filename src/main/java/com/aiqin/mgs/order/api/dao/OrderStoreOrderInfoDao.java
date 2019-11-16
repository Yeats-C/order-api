package com.aiqin.mgs.order.api.dao;

import com.aiqin.mgs.order.api.domain.OrderStoreOrderInfo;

import java.util.List;

/**
 * 订单主表操作类
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/11/13 17:28
 */
public interface OrderStoreOrderInfoDao {

    /**
     * 查询erp订单列表
     *
     * @param orderStoreOrderInfo
     * @return java.util.List<com.aiqin.mgs.order.api.domain.OrderStoreOrderInfo>
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/11/12 15:52
     */
    List<OrderStoreOrderInfo> findOrderList(OrderStoreOrderInfo orderStoreOrderInfo);

    /**
     * 根据字段精确查询订单信息
     *
     * @param orderStoreOrderInfo
     * @return java.util.List<com.aiqin.mgs.order.api.domain.OrderStoreOrderInfo>
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/11/14 15:59
     */
    List<OrderStoreOrderInfo> select(OrderStoreOrderInfo orderStoreOrderInfo);

}
