package com.aiqin.mgs.order.api.dao;

import com.aiqin.mgs.order.api.domain.CouponRule;

public interface CouponRuleDao {

    int deleteByPrimaryKey(Long id);

    int insert(CouponRule record);

    int insertSelective(CouponRule record);

    CouponRule selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(CouponRule record);

    int updateByPrimaryKey(CouponRule record);
}