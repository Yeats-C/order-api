package com.aiqin.mgs.order.api.service;

import com.aiqin.mgs.order.api.domain.CouponRule;

import java.util.List;

/**
 * description: CouponRuleService
 * date: 2020/3/11 15:10
 * author: hantao
 * version: 1.0
 */
public interface CouponRuleService {

    List<CouponRule>  getList();

    CouponRule getCouponRule(Integer couponType);

    Boolean insert(CouponRule couponRule);

    Boolean update(CouponRule couponRule);

}
