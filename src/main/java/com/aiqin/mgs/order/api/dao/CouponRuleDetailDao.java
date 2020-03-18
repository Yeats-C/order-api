package com.aiqin.mgs.order.api.dao;

import com.aiqin.mgs.order.api.domain.CouponRuleDetail;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CouponRuleDetailDao {

    int deleteByCouponType(Integer couponType);

    int insert(CouponRuleDetail record);

    int insertSelective(CouponRuleDetail record);

    List<CouponRuleDetail> selectDetailByCouponType(Integer couponType);

    int updateByPrimaryKeySelective(CouponRuleDetail record);

    int updateByPrimaryKey(CouponRuleDetail record);

    int insertBatch(@Param("records") List<CouponRuleDetail> records);
}