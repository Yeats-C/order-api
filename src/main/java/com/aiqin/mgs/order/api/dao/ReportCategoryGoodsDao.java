package com.aiqin.mgs.order.api.dao;

import com.aiqin.mgs.order.api.domain.ReportCategoryGoods;
import com.aiqin.mgs.order.api.domain.request.ReportAreaReturnSituationVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ReportCategoryGoodsDao {

    int deleteByType(Integer type);

    int insert(ReportCategoryGoods record);

    int insertSelective(ReportCategoryGoods record);

    ReportCategoryGoods selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ReportCategoryGoods record);

    int updateByPrimaryKey(ReportCategoryGoods record);

    List<ReportCategoryGoods> selectList(ReportAreaReturnSituationVo vo);

    int insertBatch(@Param("records") List<ReportCategoryGoods> records);

}