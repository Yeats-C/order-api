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

    /**
     * 根据退款单id获取退款信息
     *
     * @param refundId 退款单id
     * @return
     */
    ErpOrderRefund getOrderRefundByRefundId(String refundId);

    /**
     * 根据退款单号查询退款信息
     *
     * @param refundCode
     * @return
     */
    ErpOrderRefund getOrderRefundByRefundCode(String refundCode);

    /**
     * 根据订单id和退款单类型查询退款信息
     *
     * @param orderId        订单id
     * @param refundTypeEnum 退款类型
     * @return
     */
    ErpOrderRefund getOrderRefundByOrderIdAndRefundType(String orderId, ErpOrderRefundTypeEnum refundTypeEnum);

    /**
     * 保存退款信息
     *
     * @param po   退款信息
     * @param auth 操作人
     */
    void saveOrderRefund(ErpOrderRefund po, AuthToken auth);

    /**
     * 根据主键修改退款单非空字段
     *
     * @param po   退款信息
     * @param auth 操作人
     */
    void updateOrderRefundSelective(ErpOrderRefund po, AuthToken auth);

    /**
     * 生成退款信息
     *
     * @param orderCode 订单号
     * @param auth      操作人信息
     */
    void generateOrderRefund(String orderCode, AuthToken auth);

    /**
     * 退款单发起退款
     *
     * @param orderCode              订单号
     * @param payTransactionTypeEnum 退款交易业务类型
     * @param auth                   操作人
     */
    void orderRefundPay(String orderCode, ErpRequestPayTransactionTypeEnum payTransactionTypeEnum, AuthToken auth);

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

    /**
     * 结束退款单退款状态
     *
     * @param refundCode    退款单号
     * @param payCode       支付流水号（退款成功才传）
     * @param payStatusEnum 退款状态
     * @param auth          操作人
     */
    void endOrderRefund(String refundCode, String payCode, ErpPayStatusEnum payStatusEnum, AuthToken auth);
}
