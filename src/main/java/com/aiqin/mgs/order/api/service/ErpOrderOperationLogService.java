package com.aiqin.mgs.order.api.service;

import com.aiqin.mgs.order.api.component.enums.ErpOrderStatusEnum;
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
     * @param orderStatusEnum  订单状态
     * @param auth             手动传入操作人
     * @return void
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/11/21 10:33
     */
    void saveOrderOperationLog(String orderId, ErpOrderStatusEnum orderStatusEnum, AuthToken auth);

    /**
     * 复制日志集合到另一个订单
     *
     * @param orderId
     * @param list
     * @return void
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/3 19:08
     */
    void copySplitOrderLog(String orderId, List<OrderStoreOrderOperationLog> list);
}
