package com.aiqin.mgs.order.api.dao;

import com.aiqin.mgs.order.api.domain.OrderListLogistics;

import java.util.List;

public interface OrderListLogisticsDao {

    int deleteByPrimaryKey(Long id);

    int insert(OrderListLogistics record);

    int insertSelective(OrderListLogistics record);

    OrderListLogistics selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OrderListLogistics record);

    int updateByPrimaryKey(OrderListLogistics record);

    List<OrderListLogistics> searchOrderListLogisticsByCode(String code);

    Boolean insertLogistics(OrderListLogistics param);
}