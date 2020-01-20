package com.aiqin.mgs.order.api.service.order;

import com.aiqin.mgs.order.api.domain.AuthToken;
import com.aiqin.mgs.order.api.domain.request.order.ErpOrderPayRequest;
import com.aiqin.mgs.order.api.domain.request.order.PayCallbackRequest;
import com.aiqin.mgs.order.api.domain.response.order.ErpOrderLogisticsPayResultResponse;
import com.aiqin.mgs.order.api.domain.response.order.ErpOrderLogisticsPrintQueryResponse;

/**
 * 订单物流信息非事务操作类
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2020/1/20 12:49
 */
public interface ErpOrderLogisticsNoTransactionalService {


    /**
     * 物流单支付回调
     *
     * @param payCallbackRequest
     * @return void
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2020/1/1 15:03
     */
    void orderLogisticsPayCallback(PayCallbackRequest payCallbackRequest);

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

    /**
     * 物流费用支付凭证打印数据查询
     *
     * @param erpOrderPayRequest
     * @return com.aiqin.mgs.order.api.domain.response.order.ErpOrderLogisticsPrintQueryResponse
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/10 16:41
     */
    ErpOrderLogisticsPrintQueryResponse orderLogisticsPrintQuery(ErpOrderPayRequest erpOrderPayRequest);

    /**
     * 订单物流费用支付轮询
     *
     * @param logisticsCode 物流单号
     * @param auth          操作人
     * @return void
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2020/1/1 15:03
     */
    void orderLogisticsPayPolling(String logisticsCode, AuthToken auth);

}
