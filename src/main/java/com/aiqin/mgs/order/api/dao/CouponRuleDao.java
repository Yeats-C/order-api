package com.aiqin.mgs.order.api.dao;

import com.aiqin.mgs.order.api.domain.CouponRule;

import java.util.List;

public interface CouponRuleDao {

    int deleteByPrimaryKey(Long id);

    int insert(CouponRule record);

    int insertSelective(CouponRule record);

    CouponRule selectByCouponType(Integer couponType);

    int updateByPrimaryKeySelective(CouponRule record);

    int updateByPrimaryKey(CouponRule record);

    List<CouponRule> selectList();

}