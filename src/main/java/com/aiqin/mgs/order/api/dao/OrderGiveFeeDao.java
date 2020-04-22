package com.aiqin.mgs.order.api.dao;

import com.aiqin.mgs.order.api.domain.OrderGiveFee;

public interface OrderGiveFeeDao {

    int deleteByPrimaryKey(Long id);

    int insert(OrderGiveFee record);

    int insertSelective(OrderGiveFee record);

    OrderGiveFee selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OrderGiveFee record);

    int updateByPrimaryKey(OrderGiveFee record);
}