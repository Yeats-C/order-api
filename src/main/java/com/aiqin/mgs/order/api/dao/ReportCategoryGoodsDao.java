package com.aiqin.mgs.order.api.dao;

import com.aiqin.mgs.order.api.domain.ReportCategoryGoods;

public interface ReportCategoryGoodsDao {

    int deleteByPrimaryKey(Long id);

    int insert(ReportCategoryGoods record);

    int insertSelective(ReportCategoryGoods record);

    ReportCategoryGoods selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ReportCategoryGoods record);

    int updateByPrimaryKey(ReportCategoryGoods record);
}