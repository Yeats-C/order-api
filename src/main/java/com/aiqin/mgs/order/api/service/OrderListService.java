package com.aiqin.mgs.order.api.service;

import com.aiqin.mgs.order.api.base.PageResData;
import com.aiqin.mgs.order.api.domain.OrderList;
import com.aiqin.mgs.order.api.domain.OrderListLogistics;
import com.aiqin.mgs.order.api.domain.request.orderList.*;
import com.aiqin.mgs.order.api.domain.response.orderlistre.FirstOrderTimeRespVo;
import com.aiqin.mgs.order.api.domain.response.orderlistre.OrderSaveRespVo;
import com.aiqin.mgs.order.api.domain.response.orderlistre.OrderStockReVo;

import java.util.List;
import java.util.Map;

/**
 * 描述:
 *
 * @author zhujunchao
 * @create 2019-01-04 15:27
 */
public interface OrderListService {
    PageResData<OrderList> searchOrderList(OrderListVo param);

    OrderListDetailsVo getOrderByCode(String code);

    List<String> add(List<OrderListDetailsVo> param);

    OrderSaveRespVo save(OrderReqVo reqVo);

    /**
     * 保存订单（只保存，不支付也不锁库和拆单）
     * @param reqVo
     * @return
     */
    Boolean saveOrder(OrderReqVo reqVo);

    PageResData<OrderList> searchOrderReceptionList(OrderListVo2 param);

    Boolean addLogistics(OrderListLogistics param);

    Boolean updateOrderStatus(String code, Integer status);

    List<OrderStockReVo> getStockValue(OrderStockVo vo);

    PageResData<OrderListFather> searchOrderReceptionListFather(OrderListVo2 param);

    PageResData<OrderListFather> searchOrderListFather(OrderListVo param);

    Boolean updateOrderRefund(String code);

    List<OrderListDetailsVo> getOrderByCodeFather(String code);

    List<FirstOrderTimeRespVo> selectFirstOrderTime(List<String> storeIds);

    PageResData<OrderListFather> searchOrderReceptionListFatherProduct(OrderListVo2 param);

//    Boolean updateOrderActualDeliver(List<ActualDeliverVo> actualDeliverVos);

    Boolean updateOrderStatusDeliver(DeliverVo vo);

    Boolean updateOrderStatusReceiving(String code, String name);
}
