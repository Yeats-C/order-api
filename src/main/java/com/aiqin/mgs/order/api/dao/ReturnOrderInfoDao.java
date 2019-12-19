package com.aiqin.mgs.order.api.dao;

import com.aiqin.mgs.order.api.domain.ReturnOrderInfo;

public interface ReturnOrderInfoDao {

    int deleteByPrimaryKey(Long id);

    int insertSelective(ReturnOrderInfo record);

    ReturnOrderInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ReturnOrderInfo record);

}