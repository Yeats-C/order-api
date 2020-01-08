package com.aiqin.mgs.order.api.service.order;

import com.aiqin.mgs.order.api.domain.request.order.ErpOrderCancelRequest;

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
     * @param erpOrderCancelRequest
     * @return void
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/12 17:45
     */
    void cancelOrderWithoutStock(ErpOrderCancelRequest erpOrderCancelRequest);

    /**
     * 申请取消订单
     *
     * @param erpOrderCancelRequest
     * @return void
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/12 17:46
     */
    void applyCancelOrder(ErpOrderCancelRequest erpOrderCancelRequest);

}
