package com.aiqin.mgs.order.api.service.order;

import com.aiqin.mgs.order.api.domain.AuthToken;
import com.aiqin.mgs.order.api.domain.po.order.ErpOrderConsignee;

/**
 * 订单收货人信息
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/12/10 9:36
 */
public interface ErpOrderConsigneeService {

    /**
     * 保存订单收货人信息
     *
     * @param po
     * @param auth
     * @return void
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/10 9:37
     */
    void saveOrderConsignee(ErpOrderConsignee po, AuthToken auth);

    /**
     * 根据订单id获取订单收货人信息
     *
     * @param orderId
     * @return com.aiqin.mgs.order.api.domain.po.order.ErpOrderConsignee
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/10 9:37
     */
    ErpOrderConsignee getOrderConsigneeByOrderId(String orderId);
}
