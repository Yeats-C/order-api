package com.aiqin.mgs.order.api.service.order;

import com.aiqin.mgs.order.api.domain.request.order.ErpOrderDeliverRequest;
import com.aiqin.mgs.order.api.domain.request.order.ErpOrderTransportRequest;

/**
 * 发货service
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/12/21 11:16
 */
public interface ErpOrderDeliverService {

    /**
     * 订单出货（修改订单发货数量）
     *
     * @param erpOrderDeliverRequest
     * @return void
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2020/1/1 18:04
     */
    void orderDeliver(ErpOrderDeliverRequest erpOrderDeliverRequest);

    /**
     * 订单发运（订单关联物流单）
     *
     * @param erpOrderTransportRequest
     * @return void
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2020/1/1 18:05
     */
    void orderTransport(ErpOrderTransportRequest erpOrderTransportRequest);

    /**
     * 分配运费
     *
     * @param logisticsCode 物流单号
     * @return void
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/21 11:16
     */
    void distributeLogisticsFee(String logisticsCode);
}
