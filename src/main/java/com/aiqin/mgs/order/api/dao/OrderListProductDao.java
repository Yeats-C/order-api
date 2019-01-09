package com.aiqin.mgs.order.api.dao;

import com.aiqin.mgs.order.api.domain.OrderListProduct;

import java.util.List;

public interface OrderListProductDao {


    int deleteByPrimaryKey(Long id);

    int insert(OrderListProduct record);

    int insertSelective(OrderListProduct record);



    OrderListProduct selectByPrimaryKey(Long id);



    int updateByPrimaryKeySelective(OrderListProduct record);

    int updateByPrimaryKey(OrderListProduct record);

    List<OrderListProduct> searchOrderListProductByCode(String code);

    Boolean insertList(List<OrderListProduct> orderListProductList);
}