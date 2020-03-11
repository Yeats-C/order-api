package com.aiqin.mgs.order.api.service.impl;

import com.aiqin.mgs.order.api.dao.CouponRuleDao;
import com.aiqin.mgs.order.api.dao.CouponRuleDetailDao;
import com.aiqin.mgs.order.api.domain.CouponRule;
import com.aiqin.mgs.order.api.domain.CouponRuleDetail;
import com.aiqin.mgs.order.api.service.CouponRuleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * description: CouponRuleServiceImpl
 * date: 2020/3/11 15:23
 * author: hantao
 * version: 1.0
 */
@Slf4j
@Service
public class CouponRuleServiceImpl implements CouponRuleService {

    @Autowired
    private CouponRuleDao couponRuleDao;
    @Autowired
    private CouponRuleDetailDao couponRuleDetailDao;

    @Override
    public List<CouponRule> getList() {
        List<CouponRule> couponRules = couponRuleDao.selectList();
        for(CouponRule cr:couponRules){
            List<CouponRuleDetail> couponRuleDetails = couponRuleDetailDao.selectDetailByCouponType(cr.getCouponType());
            cr.setCouponRuleDetailList(couponRuleDetails);
        }
        return couponRules;
    }

    @Override
    public CouponRule getCouponRule(Integer couponType) {
        return couponRuleDao.selectByCouponType(couponType);
    }

    @Override
    public Boolean insert(CouponRule couponRule) {
        return couponRuleDao.insertSelective(couponRule)>0;
    }

    @Override
    public Boolean update(CouponRule couponRule) {
        return couponRuleDao.updateByPrimaryKeySelective(couponRule)>0;
    }
}
