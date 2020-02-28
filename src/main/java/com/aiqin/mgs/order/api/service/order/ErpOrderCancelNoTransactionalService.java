package com.aiqin.mgs.order.api.service.order;

import com.aiqin.mgs.order.api.domain.AuthToken;
import com.aiqin.mgs.order.api.domain.request.order.ErpOrderCancelRequest;

/**
 * 订单取消非事务操作类
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2020/1/20 12:49
 */
public interface ErpOrderCancelNoTransactionalService {

    /**
     * 取消订单后续操作流程
     *
     * @param orderCode 订单号
     * @param auth      操作人
     * @return void
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2020/1/11 10:10
     */
    void cancelOrderRequestGroup(String orderCode, AuthToken auth);

    /**
     * 缺货终止交易
     *
     * @param erpOrderCancelRequest
     * @param auth
     * @return void
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/12 17:45
     */
    void cancelOrderWithoutStock(ErpOrderCancelRequest erpOrderCancelRequest, AuthToken auth);

    /**
     * 申请取消订单
     *
     * @param erpOrderCancelRequest
     * @param auth
     * @return void
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/12 17:46
     */
    void applyCancelOrder(ErpOrderCancelRequest erpOrderCancelRequest, AuthToken auth);

}
