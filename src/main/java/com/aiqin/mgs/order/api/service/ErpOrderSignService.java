package com.aiqin.mgs.order.api.service;

import com.aiqin.mgs.order.api.domain.OrderStoreOrderInfo;

/**
 * 订单签收处理类
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/11/30 15:25
 */
public interface ErpOrderSignService {

    /**
     * 查询订单签收信息
     *
     * @param orderStoreOrderInfo
     * @return com.aiqin.mgs.order.api.domain.OrderStoreOrderInfo
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/11/30 15:25
     */
    OrderStoreOrderInfo getOrderSignDetail(OrderStoreOrderInfo orderStoreOrderInfo);

    /**
     * 订单签收操作
     *
     * @param orderStoreOrderInfo
     * @return void
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/11/30 15:25
     */
    void orderSign(OrderStoreOrderInfo orderStoreOrderInfo);
}
