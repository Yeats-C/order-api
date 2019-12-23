package com.aiqin.mgs.order.api.dao;

import com.aiqin.mgs.order.api.domain.CouponApprovalInfo;

import java.util.List;

public interface CouponApprovalInfoDao {

    int deleteByPrimaryKey(Integer id);

    int insertSelective(CouponApprovalInfo record);

    CouponApprovalInfo selectByPrimaryKey(Integer id);

    List<CouponApprovalInfo> selectList(CouponApprovalInfo record);

    int updateByPrimaryKeySelective(CouponApprovalInfo record);

    int updateByFormNoSelective(CouponApprovalInfo record);

    CouponApprovalInfo selectByFormNo(String formNo);

}