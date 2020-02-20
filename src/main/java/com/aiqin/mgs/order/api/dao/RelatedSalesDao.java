package com.aiqin.mgs.order.api.dao;

import com.aiqin.mgs.order.api.domain.RelatedSales;
import com.aiqin.mgs.order.api.domain.request.RelatedSalesVo;

import java.util.List;

public interface RelatedSalesDao {

    int deleteByPrimaryKey(Long id);

    int insertSelective(RelatedSales record);

    RelatedSales selectBySalseCategoryId(String salseCategoryId);

    int updateByPrimaryKeySelective(RelatedSales record);

    int updateStatus(RelatedSales record);

    List<RelatedSales> selectList(RelatedSalesVo record);

}