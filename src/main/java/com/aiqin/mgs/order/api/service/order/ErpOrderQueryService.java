package com.aiqin.mgs.order.api.service.order;

import com.aiqin.mgs.order.api.domain.po.order.ErpOrderInfo;

import java.util.List;

/**
 * 订单查询service
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/11/21 10:51
 */
public interface ErpOrderQueryService {

    ErpOrderInfo getOrderByOrderId(String orderId);

    ErpOrderInfo getOrderByOrderCode(String orderCode);

    List<ErpOrderInfo> selectOrderBySelective(ErpOrderInfo po);

}
