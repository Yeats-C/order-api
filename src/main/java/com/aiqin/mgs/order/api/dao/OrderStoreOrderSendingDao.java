package com.aiqin.mgs.order.api.dao;

import com.aiqin.mgs.order.api.domain.OrderStoreOrderSending;
import org.apache.ibatis.annotations.Param;

/**
 * 订单发货信息
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/11/14 11:41
 */
public interface OrderStoreOrderSendingDao {

    /**
     * 根据订单id获取发货信息
     *
     * @param orderId
     * @return com.aiqin.mgs.order.api.domain.OrderStoreOrderSending
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/11/14 15:46
     */
    OrderStoreOrderSending getOrderSendingByOrderId(@Param("orderId") String orderId);

}
