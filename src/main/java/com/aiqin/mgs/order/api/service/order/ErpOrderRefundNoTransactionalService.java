package com.aiqin.mgs.order.api.service.order;

import com.aiqin.mgs.order.api.domain.AuthToken;
import com.aiqin.mgs.order.api.domain.request.order.PayCallbackRequest;

/**
 * 订单退款非事务操作类
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2020/1/20 12:49
 */
public interface ErpOrderRefundNoTransactionalService {

    /**
     * 退款回调
     *
     * @param payCallbackRequest
     */
    void orderRefundCallback(PayCallbackRequest payCallbackRequest);

    /**
     * 退款单轮询退款结果
     *
     * @param orderCode 订单号
     * @param auth      操作人
     */
    void orderRefundPolling(String orderCode, AuthToken auth);

}
