package com.aiqin.mgs.order.api.domain.response;

import com.aiqin.mgs.order.api.domain.*;
import lombok.Data;

import java.util.List;

/**
 * 订单详情
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/11/14 16:28
 */
@Data
public class ErpOrderDetailResponse {

    /***订单主表信息*/
    private OrderStoreOrderInfo orderInfo;

    /***订单商品列表*/
    private List<OrderStoreOrderProductItem> orderProductItemList;

    /***订单支付信息*/
    private OrderStoreOrderPay orderPay;

    /***订单收货信息*/
    private OrderStoreOrderReceiving orderReceiving;

    /***订单发货信息*/
    private OrderStoreOrderSending orderSending;

    /***订单操作日志列表*/
    private List<OrderStoreOrderOperationLog> orderOperationLogList;

}
