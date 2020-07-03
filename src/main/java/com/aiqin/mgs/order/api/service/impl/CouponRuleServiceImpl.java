package com.aiqin.mgs.order.api.service.impl;

import com.aiqin.mgs.order.api.dao.CouponRuleDao;
import com.aiqin.mgs.order.api.dao.CouponRuleDetailDao;
import com.aiqin.mgs.order.api.domain.CouponRule;
import com.aiqin.mgs.order.api.domain.CouponRuleDetail;
import com.aiqin.mgs.order.api.service.CouponRuleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        CouponRule couponRule = couponRuleDao.selectByCouponType(couponType);
        if(couponRule!=null&&couponRule.getCouponType()!=null){
            List<CouponRuleDetail> couponRuleDetails = couponRuleDetailDao.selectDetailByCouponType(couponType);
            couponRule.setCouponRuleDetailList(couponRuleDetails);
        }
        return couponRule;
    }

    @Override
    public Boolean update(CouponRule couponRule) {
        CouponRule couponRule1 = couponRuleDao.selectByCouponType(couponRule.getCouponType());
        if(couponRule1!=null){
            couponRule.setUpdateTime(new Date());
            couponRuleDao.updateBycouponType(couponRule);
        }else {
            couponRule.setCreateTime(new Date());
            couponRuleDao.insertSelective(couponRule);
        }
        if(couponRule.getCouponRuleDetailList()!=null&&couponRule.getCouponRuleDetailList().size()>0){
            List<CouponRuleDetail> couponRuleDetails = couponRuleDetailDao.selectDetailByCouponType(couponRule.getCouponType());
            if(couponRuleDetails!=null&&couponRuleDetails.size()>0){
                couponRuleDetailDao.deleteByCouponType(couponRule.getCouponType());
            }
            List<CouponRuleDetail> couponRuleDetailList = couponRule.getCouponRuleDetailList();
            couponRuleDetailList.forEach(p->p.setCreateTime(new Date()));
            couponRuleDetailList.forEach(p->p.setCouponType(couponRule.getCouponType()));
            couponRuleDetailDao.insertBatch(couponRuleDetailList);
        }
        return true;
    }

//    @Override
//    public Boolean update(CouponRule couponRule) {
//        return couponRuleDao.updateByPrimaryKeySelective(couponRule)>0;
//    }

    @Override
    public Map<String,BigDecimal> couponRuleMap(){
        Map<String,BigDecimal> dateMap=new HashMap();
        CouponRule couponRule=getCouponRule(2);
        if(null!=couponRule &&null!=couponRule.getCouponRuleDetailList()&&0<couponRule.getCouponRuleDetailList().size()){
            for (CouponRuleDetail detail:couponRule.getCouponRuleDetailList()) {
                dateMap.put(String.valueOf(Integer.valueOf(detail.getProductPropertyCode())),couponRule.getProportion().divide(new BigDecimal(100)));
            }
        }
        return dateMap;
    };
}
