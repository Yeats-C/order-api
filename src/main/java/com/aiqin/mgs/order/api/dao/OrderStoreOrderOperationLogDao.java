package com.aiqin.mgs.order.api.dao;

import com.aiqin.mgs.order.api.domain.OrderStoreOrderOperationLog;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 订单操作日志
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/11/14 13:55
 */
public interface OrderStoreOrderOperationLogDao {

    /**
     * 根据订单id查询订单操作日志
     *
     * @param orderId
     * @return java.util.List<com.aiqin.mgs.order.api.domain.OrderStoreOrderOperationLog>
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/11/14 15:42
     */
    List<OrderStoreOrderOperationLog> selectOperationLogListByOrderId(@Param("orderId") String orderId);

}
