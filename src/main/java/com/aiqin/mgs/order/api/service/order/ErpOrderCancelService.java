package com.aiqin.mgs.order.api.service.order;

import com.aiqin.mgs.order.api.domain.po.order.ErpOrderInfo;
import com.aiqin.mgs.order.api.domain.request.order.ErpOrderCancelResultRequest;

/**
 * 订单取消
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/12/12 17:46
 */
public interface ErpOrderCancelService {

    /**
     * 缺货终止交易
     *
     * @param erpOrderInfo
     * @return void
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/12 17:45
     */
    void cancelOrderWithoutStock(ErpOrderInfo erpOrderInfo);

    /**
     * 拒收终止交易
     *
     * @param erpOrderInfo
     * @return void
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/12 17:46
     */
    void cancelOrderRejectSign(ErpOrderInfo erpOrderInfo);

    /**
     * 申请取消订单
     *
     * @param erpOrderInfo
     * @return void
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/12 17:46
     */
    void applyCancelOrder(ErpOrderInfo erpOrderInfo);

    /**
     * 供应链返回订单是否可以取消
     *
     * @param erpOrderCancelResultRequest
     * @return void
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/12 19:23
     */
    void orderCancelResultCallback(ErpOrderCancelResultRequest erpOrderCancelResultRequest);
}
