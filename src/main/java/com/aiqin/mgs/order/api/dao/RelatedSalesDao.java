package com.aiqin.mgs.order.api.dao;

import com.aiqin.mgs.order.api.domain.RelatedSales;
import com.aiqin.mgs.order.api.domain.request.RelatedSalesVo;

import java.util.List;

public interface RelatedSalesDao {

    int deleteByPrimaryKey(Long id);

    int insert(RelatedSales record);

    int insertSelective(RelatedSales record);

    RelatedSales selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(RelatedSales record);

    int updateByPrimaryKey(RelatedSales record);

    List<RelatedSales> selectList(RelatedSalesVo record);

}