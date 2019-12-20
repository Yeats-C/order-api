package com.aiqin.mgs.order.api.dao;

import com.aiqin.mgs.order.api.domain.CouponInfo;

public interface CouponInfoDao {

    int deleteByPrimaryKey(Integer id);

    int insertSelective(CouponInfo record);

    CouponInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CouponInfo record);

}