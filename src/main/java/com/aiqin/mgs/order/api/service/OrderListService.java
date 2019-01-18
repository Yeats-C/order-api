package com.aiqin.mgs.order.api.service;

import com.aiqin.mgs.order.api.base.PageResData;
import com.aiqin.mgs.order.api.domain.OrderList;
import com.aiqin.mgs.order.api.domain.OrderListLogistics;
import com.aiqin.mgs.order.api.domain.request.orderList.*;
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

    Boolean save(OrderReqVo reqVo);

    PageResData<OrderList> searchOrderReceptionList(OrderListVo2 param);

    Boolean addLogistics(OrderListLogistics param);

    Boolean updateOrderStatus(String code, Integer status);

    List<OrderStockReVo> getStockValue(OrderStockVo vo);
}
