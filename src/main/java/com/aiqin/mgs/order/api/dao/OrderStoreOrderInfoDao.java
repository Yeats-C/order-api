package com.aiqin.mgs.order.api.dao;

import com.aiqin.mgs.order.api.domain.OrderStoreOrderInfo;
import org.apache.ibatis.annotations.Param;

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
     * 查询子订单列表
     *
     * @param primaryOrderCodeList
     * @return java.util.List<com.aiqin.mgs.order.api.domain.OrderStoreOrderInfo>
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/11/16 16:32
     */
    List<OrderStoreOrderInfo> findSecondaryOrderList(@Param("primaryOrderCodeList") List<String> primaryOrderCodeList);

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

    /**
     * 根据订单id获取订单
     *
     * @param orderId
     * @return com.aiqin.mgs.order.api.domain.OrderStoreOrderInfo
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/11/19 11:29
     */
    OrderStoreOrderInfo selectByOrderId(@Param("orderId") String orderId);

    /**
     * 根据订单编码获取订单
     *
     * @param orderCode
     * @return com.aiqin.mgs.order.api.domain.OrderStoreOrderInfo
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/11/19 11:29
     */
    OrderStoreOrderInfo selectByOrderCode(@Param("orderCode") String orderCode);

    /**
     * 根据主键更新非空字段（部分字段除外）
     *
     * @param orderStoreOrderInfo
     * @return java.lang.Integer
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/11/20 10:23
     */
    Integer updateByPrimaryKeySelective(OrderStoreOrderInfo orderStoreOrderInfo);

    /**
     * 插入订单数据
     *
     * @param orderStoreOrderInfo
     * @return java.lang.Integer
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/11/20 11:10
     */
    Integer insert(OrderStoreOrderInfo orderStoreOrderInfo);

}
