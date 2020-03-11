package com.aiqin.mgs.order.api.dao;

import com.aiqin.mgs.order.api.domain.CouponRuleDetail;

public interface CouponRuleDetailDao {

    int deleteByPrimaryKey(Long id);

    int insert(CouponRuleDetail record);

    int insertSelective(CouponRuleDetail record);

    CouponRuleDetail selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(CouponRuleDetail record);

    int updateByPrimaryKey(CouponRuleDetail record);
}