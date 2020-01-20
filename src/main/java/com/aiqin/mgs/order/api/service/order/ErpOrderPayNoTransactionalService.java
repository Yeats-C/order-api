package com.aiqin.mgs.order.api.service.order;

import com.aiqin.mgs.order.api.domain.AuthToken;
import com.aiqin.mgs.order.api.domain.request.order.ErpOrderPayRequest;
import com.aiqin.mgs.order.api.domain.request.order.PayCallbackRequest;
import com.aiqin.mgs.order.api.domain.response.order.ErpOrderPayResultResponse;

/**
 * 订单支付非事务操作类
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2020/1/20 12:49
 */
public interface ErpOrderPayNoTransactionalService {

    /**
     * 发起支付或者创建订单自动发起支付时调用
     *
     * @param erpOrderPayRequest 支付信息
     * @param auth               操作人
     * @param authCheck          自动支付校验
     * @return void
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2020/1/1 15:04
     */
    void orderPayStartMethodGroup(ErpOrderPayRequest erpOrderPayRequest, AuthToken auth, boolean authCheck);

    /**
     * 轮询成功或者回调成功调用
     *
     * @param orderCode 订单号
     * @param auth      操作人
     * @return void
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2020/1/1 15:04
     */
    void orderPaySuccessMethodGroup(String orderCode, AuthToken auth);

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
     * @param orderCode 订单号
     * @param auth      操作人
     * @return void
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/10 9:48
     */
    void orderPayPolling(String orderCode, AuthToken auth);

    /**
     * 支付回调方法
     *
     * @param payCallbackRequest
     * @return java.lang.String
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/10 9:48
     */
    void orderPayCallback(PayCallbackRequest payCallbackRequest);


}
