package com.aiqin.mgs.order.api.service.order;

import com.aiqin.mgs.order.api.domain.AuthToken;
import com.aiqin.mgs.order.api.domain.po.order.ErpOrderLogistics;
import com.aiqin.mgs.order.api.domain.po.order.ErpOrderPay;
import com.aiqin.mgs.order.api.domain.request.order.ErpOrderPayCallbackRequest;
import com.aiqin.mgs.order.api.domain.request.order.ErpOrderPayRequest;
import com.aiqin.mgs.order.api.domain.response.order.ErpOrderLogisticsPayResultResponse;
import com.aiqin.mgs.order.api.domain.response.order.ErpOrderPayResultResponse;

/**
 * 订单支付service
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/11/20 15:26
 */
public interface ErpOrderPayService {

    /**
     * 根据支付id获取支付信息
     *
     * @param payId 支付id
     * @return com.aiqin.mgs.order.api.domain.po.order.ErpOrderPay
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/10 9:46
     */
    ErpOrderPay getOrderPayByPayId(String payId);

    /**
     * 保存支付信息
     *
     * @param po
     * @param auth
     * @return void
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/10 9:46
     */
    void saveOrderPay(ErpOrderPay po, AuthToken auth);

    /**
     * 根据主键更新支付信息
     *
     * @param po
     * @param auth
     * @return void
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/10 9:47
     */
    void updateOrderPaySelective(ErpOrderPay po, AuthToken auth);

    /**
     * 订单支付
     *
     * @param erpOrderPayRequest
     * @return void
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/10 9:47
     */
    String orderPay(ErpOrderPayRequest erpOrderPayRequest);

    /**
     * 查询订单支付结果
     *
     * @param erpOrderPayRequest
     * @return com.aiqin.mgs.order.api.domain.response.order.ErpOrderPayResultResponse
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/10 9:47
     */
    ErpOrderPayResultResponse orderPayResult(ErpOrderPayRequest erpOrderPayRequest);

    /**
     * 轮询查询支付结果
     *
     * @param payId 支付id
     * @return void
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/10 9:48
     */
    void payPolling(String payId);

    /**
     * 支付回调方法
     *
     * @param erpOrderPayCallbackRequest
     * @return java.lang.String
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/10 9:48
     */
    void orderPayCallback(ErpOrderPayCallbackRequest erpOrderPayCallbackRequest);

    /**
     * 支付完成后续处理
     *
     * @param payId  支付id
     * @param manual 是否人工操作
     * @return java.lang.String
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/10 9:48
     */
    String endPay(String payId, boolean manual);

    /**
     * 校验并更正订单支付信息
     *
     * @param erpOrderPayRequest
     * @return void
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/10 9:48
     */
    void orderPayRepay(ErpOrderPayRequest erpOrderPayRequest);

    /**
     * 订单超时未支付取消订单
     *
     * @param erpOrderPayRequest
     * @return void
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/10 9:49
     */
    void orderTimeoutUnpaid(ErpOrderPayRequest erpOrderPayRequest);

    /**
     * 获取需要支付的物流费用信息
     *
     * @param erpOrderPayRequest
     * @return com.aiqin.mgs.order.api.domain.po.order.ErpOrderLogistics
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/10 15:13
     */
    ErpOrderLogistics getOrderLogisticsFee(ErpOrderPayRequest erpOrderPayRequest);

    /**
     * 发起支付物流费用
     *
     * @param erpOrderPayRequest
     * @return java.lang.String
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/10 15:41
     */
    String orderLogisticsPay(ErpOrderPayRequest erpOrderPayRequest);

    /**
     * 查询订单物流费用支付结果
     *
     * @param erpOrderPayRequest
     * @return com.aiqin.mgs.order.api.domain.response.order.ErpOrderLogisticsPayResultResponse
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/10 15:31
     */
    ErpOrderLogisticsPayResultResponse orderLogisticsPayResult(ErpOrderPayRequest erpOrderPayRequest);
}
