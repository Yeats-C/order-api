package com.aiqin.mgs.order.api.service.order;

import com.aiqin.mgs.order.api.domain.AuthToken;
import com.aiqin.mgs.order.api.domain.po.order.ErpOrderPay;

/**
 * 订单支付service
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/11/20 15:26
 */
public interface ErpOrderPayService {

    void saveOrderPay(ErpOrderPay po, AuthToken auth);
}
