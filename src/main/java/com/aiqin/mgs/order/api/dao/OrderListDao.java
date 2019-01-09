package com.aiqin.mgs.order.api.dao;

import com.aiqin.mgs.order.api.domain.OrderList;
import com.aiqin.mgs.order.api.domain.request.orderList.OrderListDetailsVo;
import com.aiqin.mgs.order.api.domain.request.orderList.OrderListVo;
import com.aiqin.mgs.order.api.domain.request.orderList.OrderListVo2;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrderListDao {


    int deleteByPrimaryKey(Long id);

    int insert(OrderList record);

    int insertSelective(OrderList record);



    OrderList selectByPrimaryKey(Long id);



    int updateByPrimaryKeySelective(OrderList record);

    int updateByPrimaryKey(OrderList record);

    List<OrderList> searchOrderList(OrderListVo param);

    int searchOrderListCount(OrderListVo param);

    OrderListDetailsVo searchOrderByCode(String code);

    Boolean insertOrderListDetailsVo(OrderListDetailsVo param);

    List<OrderList> searchOrderReceptionList(OrderListVo2 param);

    int searchOrderReceptionListCount(OrderListVo2 param);

    Boolean updateByCode(@Param("code") String code, @Param("status") Integer status, @Param("paymentStatus") Integer paymentStatus);
}