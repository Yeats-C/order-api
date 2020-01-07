package com.aiqin.mgs.order.api.service.order;

import com.aiqin.mgs.order.api.component.enums.ErpOrderRefundTypeEnum;
import com.aiqin.mgs.order.api.domain.AuthToken;
import com.aiqin.mgs.order.api.domain.po.order.ErpOrderRefund;

/**
 * 订单退款信息service
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/11/20 15:26
 */
public interface ErpOrderRefundService {

    ErpOrderRefund getOrderRefundByRefundId(String refundId);

    ErpOrderRefund getOrderRefundByOrderIdAndRefundType(String orderId, ErpOrderRefundTypeEnum refundTypeEnum);

    void saveOrderRefund(ErpOrderRefund po, AuthToken auth);

    void updateOrderRefundSelective(ErpOrderRefund po, AuthToken auth);

}
