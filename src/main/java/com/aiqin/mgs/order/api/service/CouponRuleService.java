package com.aiqin.mgs.order.api.service;

import com.aiqin.mgs.order.api.domain.CouponRule;

import java.util.List;
import java.util.Map;

/**
 * description: CouponRuleService
 * date: 2020/3/11 15:10
 * author: hantao
 * version: 1.0
 */
public interface CouponRuleService {

    /**
     * 基础设置--活惠券规则设置列表
     * @return
     */
    List<CouponRule> getList();

    /**
     * 基础设置--根据活惠券类型查看规则及商品属性
     * 优惠券类型 0-物流券 1-服纺券 2-A品券
     * @param couponType
     * @return
     */
    CouponRule getCouponRule(Integer couponType);

    /**
     * 基础设置--活惠券规则设置
     * @param couponRule
     * @return
     */
//    Boolean insert(CouponRule couponRule);

    /**
     * 基础设置--活惠券规则修改
     * @param couponRule
     * @return
     */
    Boolean update(CouponRule couponRule);

    /**
     * 查询当前A品卷规则map
     * @return
     */
     Map couponRuleMap();

}
