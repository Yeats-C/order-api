package com.aiqin.mgs.order.api.dao;

import com.aiqin.mgs.order.api.domain.CouponInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CouponInfoDao {

    int deleteByPrimaryKey(Integer id);

    int insertSelective(CouponInfo record);

    CouponInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CouponInfo record);

    int insertBatch(@Param("records") List<CouponInfo> records);

}