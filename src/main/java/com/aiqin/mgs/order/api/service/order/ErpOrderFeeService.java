package com.aiqin.mgs.order.api.service.order;

import com.aiqin.mgs.order.api.domain.AuthToken;
import com.aiqin.mgs.order.api.domain.po.order.ErpOrderFee;

/**
 * 订单费用信息service
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/11/21 11:00
 */
public interface ErpOrderFeeService {

    /**
     * 保存订单费用信息
     *
     * @param po
     * @param auth
     * @return void
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/11 20:47
     */
    void saveOrderFee(ErpOrderFee po, AuthToken auth);

    /**
     * 根据主键更新数据（非空字段）
     *
     * @param po
     * @param auth
     * @return void
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/11 20:52
     */
    void updateOrderFeeByPrimaryKeySelective(ErpOrderFee po, AuthToken auth);

    /**
     * 根据feeId获取订单费用信息
     *
     * @param feeId 费用id
     * @return com.aiqin.mgs.order.api.domain.po.order.ErpOrderFee
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/11 20:57
     */
    ErpOrderFee getOrderFeeByFeeId(String feeId);

    /**
     * 根据支付id获取订单费用信息
     *
     * @param payId 支付单id
     * @return com.aiqin.mgs.order.api.domain.po.order.ErpOrderFee
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/11 20:52
     */
    ErpOrderFee getOrderFeeByPayId(String payId);

    /**
     * 根据订单id获取订单费用信息
     *
     * @param orderId 订单id
     * @return com.aiqin.mgs.order.api.domain.po.order.ErpOrderFee
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/11 20:52
     */
    ErpOrderFee getOrderFeeByOrderId(String orderId);
}
