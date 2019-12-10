package com.aiqin.mgs.order.api.service.order;

import com.aiqin.mgs.order.api.component.enums.ErpOrderStatusEnum;
import com.aiqin.mgs.order.api.domain.AuthToken;
import com.aiqin.mgs.order.api.domain.po.order.ErpOrderOperationLog;

import java.util.List;

/**
 * 订单操作日志service
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/12/10 9:43
 */
public interface ErpOrderOperationLogService {

    /**
     * 根据订单id查询订单操作日志
     *
     * @param orderId 订单id
     * @return java.util.List<com.aiqin.mgs.order.api.domain.po.order.ErpOrderOperationLog>
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/10 9:43
     */
    List<ErpOrderOperationLog> selectOrderOperationLogListByOrderId(String orderId);

    /**
     * 保存订单日志
     *
     * @param orderId         订单id
     * @param orderStatusEnum 订单状态
     * @param auth            操作人
     * @return void
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/10 9:44
     */
    void saveOrderOperationLog(String orderId, ErpOrderStatusEnum orderStatusEnum, AuthToken auth);

    /**
     * 复制订单操作日志给另一个订单
     *
     * @param orderId
     * @param list
     * @return void
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/10 9:44
     */
    void copySplitOrderLog(String orderId, List<ErpOrderOperationLog> list);
}
