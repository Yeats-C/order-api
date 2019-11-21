package com.aiqin.mgs.order.api.service;

import com.aiqin.mgs.order.api.domain.AuthToken;
import com.aiqin.mgs.order.api.domain.OrderStoreOrderOperationLog;

import java.util.List;

/**
 * 订单操作日志service
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/11/20 15:08
 */
public interface ErpOrderOperationLogService {

    /**
     * 根据订单id查询操作日志
     *
     * @param orderId
     * @return java.util.List<com.aiqin.mgs.order.api.domain.OrderStoreOrderOperationLog>
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/11/20 15:08
     */
    List<OrderStoreOrderOperationLog> selectOperationLogListByOrderId(String orderId);

    /**
     * 插入订单操作日志，无请求用户
     *
     * @param orderId          订单id
     * @param operationContent 操作动作
     * @param auth             手动传入操作人
     * @return void
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/11/21 10:33
     */
    void saveOrderOperationLog(String orderId, String operationContent, AuthToken auth);
}
