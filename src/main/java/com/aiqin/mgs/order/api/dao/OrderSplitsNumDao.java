package com.aiqin.mgs.order.api.dao;

import com.aiqin.mgs.order.api.domain.OrderSplitsNum;

public interface OrderSplitsNumDao {

    int deleteByPrimaryKey(Long id);

    int insert(OrderSplitsNum record);

    int insertSelective(OrderSplitsNum record);

    OrderSplitsNum selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OrderSplitsNum record);

    int updateByPrimaryKey(OrderSplitsNum record);

    OrderSplitsNum selectByOrderCode(String orderCode);

    int updateByOrderCode(OrderSplitsNum record);
}