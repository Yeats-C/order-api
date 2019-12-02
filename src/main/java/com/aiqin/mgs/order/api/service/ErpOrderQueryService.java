package com.aiqin.mgs.order.api.service;

import com.aiqin.mgs.order.api.domain.OrderStoreOrderInfo;

import java.util.List;

/**
 * 订单查询封装原子操作service
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/11/21 10:51
 */
public interface ErpOrderQueryService {

    /**
     * 根据订单id获取订单信息
     *
     * @param orderId
     * @return com.aiqin.mgs.order.api.domain.OrderStoreOrderInfo
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/11/21 10:58
     */
    OrderStoreOrderInfo getOrderByOrderId(String orderId);

    /**
     * 根据订单code获取订单信息
     *
     * @param orderCode
     * @return com.aiqin.mgs.order.api.domain.OrderStoreOrderInfo
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/11/21 10:58
     */
    OrderStoreOrderInfo getOrderByOrderCode(String orderCode);

    /**
     * 根据非空字段精确筛选订单
     *
     * @param orderInfo
     * @return java.util.List<com.aiqin.mgs.order.api.domain.OrderStoreOrderInfo>
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/11/21 10:59
     */
    List<OrderStoreOrderInfo> selectOrderBySelective(OrderStoreOrderInfo orderInfo);

}
