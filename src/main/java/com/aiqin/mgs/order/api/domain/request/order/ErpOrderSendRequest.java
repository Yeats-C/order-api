package com.aiqin.mgs.order.api.domain.request.order;

import com.aiqin.mgs.order.api.domain.OrderStoreOrderInfo;
import com.aiqin.mgs.order.api.domain.OrderStoreOrderSending;
import lombok.Data;

import java.util.List;

/**
 * 订单发货请求参数
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/12/5 18:02
 */
@Data
public class ErpOrderSendRequest {

    /***订单物流信息*/
    private OrderStoreOrderSending orderSending;

    /***订单信息*/
    private List<OrderStoreOrderInfo> orderList;

}
