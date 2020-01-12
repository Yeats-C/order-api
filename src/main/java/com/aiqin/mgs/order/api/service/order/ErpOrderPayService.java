package com.aiqin.mgs.order.api.service.order;

import com.aiqin.mgs.order.api.component.enums.pay.ErpPayStatusEnum;
import com.aiqin.mgs.order.api.domain.AuthToken;
import com.aiqin.mgs.order.api.domain.po.order.ErpOrderPay;
import com.aiqin.mgs.order.api.domain.request.order.ErpOrderPayRequest;
import com.aiqin.mgs.order.api.domain.request.order.PayCallbackRequest;
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
     * 订单支付
     *
     * @param erpOrderPayRequest
     * @param auth
     * @return void
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/10 9:47
     */
    void orderPay(ErpOrderPayRequest erpOrderPayRequest, AuthToken auth, boolean autoCheck);

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
     * 完成订单支付状态
     *
     * @param orderCode     订单号
     * @param payCode       支付流水号
     * @param payStatusEnum 支付结果
     * @param auth          操作人
     * @param manual        是否人工操作
     * @return void
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/30 14:49
     */
    void endOrderPay(String orderCode, String payCode, ErpPayStatusEnum payStatusEnum, AuthToken auth, boolean manual);

    /**
     * 支付成功后获取物流券
     *
     * @param orderCode 订单号
     * @param auth      操作人
     * @return void
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/30 15:10
     */
    void getGoodsCoupon(String orderCode, AuthToken auth);

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

    /**
     * 订单超时未支付取消订单
     *
     * @param orderCode 订单号
     * @return void
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/10 9:49
     */
    void orderTimeoutUnpaid(String orderCode);


}
