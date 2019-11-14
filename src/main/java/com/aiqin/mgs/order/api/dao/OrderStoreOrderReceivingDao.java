package com.aiqin.mgs.order.api.dao;

import com.aiqin.mgs.order.api.domain.OrderStoreOrderReceiving;
import org.apache.ibatis.annotations.Param;

/**
 * 订单收货信息
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/11/14 11:31
 */
public interface OrderStoreOrderReceivingDao {

    /**
     * 根据订单id查询收货信息
     *
     * @param orderId
     * @return com.aiqin.mgs.order.api.domain.OrderStoreOrderReceiving
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/11/14 15:50
     */
    OrderStoreOrderReceiving getOrderReceivingByOrderId(@Param("orderId") String orderId);
}
