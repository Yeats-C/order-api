package com.aiqin.mgs.order.api.service.order;

import com.aiqin.mgs.order.api.domain.AuthToken;
import com.aiqin.mgs.order.api.domain.po.order.ErpOrderLogistics;

/**
 * 订单物流信息
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/12/9 19:19
 */
public interface ErpOrderLogisticsService {

    /**
     * 保存订单物流信息
     *
     * @param po
     * @param auth
     * @return void
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/10 9:41
     */
    void saveOrderLogistics(ErpOrderLogistics po, AuthToken auth);

    /**
     * 根据支付id获取订单物流信息
     *
     * @param payId 支付id
     * @return com.aiqin.mgs.order.api.domain.po.order.ErpOrderLogistics
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/10 9:41
     */
    ErpOrderLogistics getOrderLogisticsByPayId(String payId);

    /**
     * 根据物流id获取物流信息
     *
     * @param logisticsId 物流id
     * @return com.aiqin.mgs.order.api.domain.po.order.ErpOrderLogistics
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/10 9:42
     */
    ErpOrderLogistics getOrderLogisticsByLogisticsId(String logisticsId);

    /**
     * 根据物流单号获取物流信息
     *
     * @param logisticsCode 物流单号
     * @return com.aiqin.mgs.order.api.domain.po.order.ErpOrderLogistics
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/10 9:42
     */
    ErpOrderLogistics getOrderLogisticsByLogisticsCode(String logisticsCode);

    /**
     * 根据主键更新物流信息
     *
     * @param po
     * @param auth
     * @return void
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/10 9:43
     */
    void updateOrderLogisticsSelective(ErpOrderLogistics po, AuthToken auth);
}
