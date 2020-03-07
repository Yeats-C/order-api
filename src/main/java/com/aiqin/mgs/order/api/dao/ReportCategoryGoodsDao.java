package com.aiqin.mgs.order.api.dao;

import com.aiqin.mgs.order.api.domain.ReportCategoryGoods;
import com.aiqin.mgs.order.api.domain.request.ReportAreaReturnSituationVo;

import java.util.List;

public interface ReportCategoryGoodsDao {

    int deleteByPrimaryKey(Long id);

    int insert(ReportCategoryGoods record);

    int insertSelective(ReportCategoryGoods record);

    ReportCategoryGoods selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ReportCategoryGoods record);

    int updateByPrimaryKey(ReportCategoryGoods record);

    List<ReportCategoryGoods> selectList(ReportAreaReturnSituationVo vo);

}