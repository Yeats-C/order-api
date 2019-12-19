package com.aiqin.mgs.order.api.dao;

import com.aiqin.mgs.order.api.domain.ReturnOrderDetail;

public interface ReturnOrderDetailDao {

    int deleteByPrimaryKey(Long id);

    int insertSelective(ReturnOrderDetail record);

    ReturnOrderDetail selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ReturnOrderDetail record);

}