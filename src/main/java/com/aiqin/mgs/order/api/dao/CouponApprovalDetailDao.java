package com.aiqin.mgs.order.api.dao;

import com.aiqin.mgs.order.api.domain.CouponApprovalDetail;

public interface CouponApprovalDetailDao {

    int deleteByPrimaryKey(Integer id);

    int insertSelective(CouponApprovalDetail record);

    CouponApprovalDetail selectByFormNo(String formNo);

    int updateByPrimaryKeySelective(CouponApprovalDetail record);

}