package com.aiqin.mgs.order.api.service.order;

import com.aiqin.mgs.order.api.component.enums.ErpOrderNodeProcessTypeEnum;
import com.aiqin.mgs.order.api.component.enums.ErpOrderNodeStatusEnum;
import com.aiqin.mgs.order.api.component.enums.ErpOrderStatusEnum;
import com.aiqin.mgs.order.api.domain.AuthToken;
import com.aiqin.mgs.order.api.domain.request.order.ErpOrderCancelRequest;

/**
 * 订单取消
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/12/12 17:46
 */
public interface ErpOrderCancelService {

    void cancelOrderRequestGroup(String orderCode, AuthToken auth);

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

    void turnOffCoupon(String orderCode, AuthToken auth);
    void unlockStock(String orderCode, ErpOrderNodeProcessTypeEnum processTypeEnum, boolean skipStep, AuthToken auth);

    void cancelPurchaseOrder(String orderCode, ErpOrderNodeProcessTypeEnum processTypeEnum, AuthToken auth);

    void cancelOrderStatus(String orderCode, ErpOrderStatusEnum orderStatusEnum, ErpOrderNodeStatusEnum orderNodeStatusEnum, AuthToken auth);

}
