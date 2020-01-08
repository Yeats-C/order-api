package com.aiqin.mgs.order.api.service.order;

import com.aiqin.mgs.order.api.component.enums.ErpOrderRefundTypeEnum;
import com.aiqin.mgs.order.api.component.enums.pay.ErpPayStatusEnum;
import com.aiqin.mgs.order.api.component.enums.pay.ErpRequestPayTransactionTypeEnum;
import com.aiqin.mgs.order.api.domain.AuthToken;
import com.aiqin.mgs.order.api.domain.po.order.ErpOrderRefund;
import com.aiqin.mgs.order.api.domain.request.order.PayCallbackRequest;

/**
 * 订单退款信息service
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/11/20 15:26
 */
public interface ErpOrderRefundService {

    ErpOrderRefund getOrderRefundByRefundId(String refundId);

    ErpOrderRefund getOrderRefundByRefundCode(String refundCode);

    ErpOrderRefund getOrderRefundByOrderIdAndRefundType(String orderId, ErpOrderRefundTypeEnum refundTypeEnum);

    void saveOrderRefund(ErpOrderRefund po, AuthToken auth);

    void updateOrderRefundSelective(ErpOrderRefund po, AuthToken auth);

    void orderRefundPay(String orderCode, ErpRequestPayTransactionTypeEnum payTransactionTypeEnum, AuthToken auth);

    void orderRefundCallback(PayCallbackRequest payCallbackRequest);

    void orderRefundPolling(String orderCode, AuthToken auth);

    void endOrderRefund(String refundCode, String payCode, ErpPayStatusEnum payStatusEnum, AuthToken auth);
}
