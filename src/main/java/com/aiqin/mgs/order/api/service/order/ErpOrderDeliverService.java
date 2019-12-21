package com.aiqin.mgs.order.api.service.order;

import com.aiqin.mgs.order.api.domain.request.order.ErpOrderDeliverRequest;

/**
 * 发货service
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/12/21 11:16
 */
public interface ErpOrderDeliverService {

    /**
     * 订单发货
     *
     * @param erpOrderDeliverRequest
     * @return void
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/10 9:39
     */
    void orderDeliver(ErpOrderDeliverRequest erpOrderDeliverRequest);

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
