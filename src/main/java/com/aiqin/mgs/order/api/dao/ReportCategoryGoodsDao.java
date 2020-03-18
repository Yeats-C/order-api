package com.aiqin.mgs.order.api.dao;

import com.aiqin.mgs.order.api.domain.ReportCategoryGoods;
import com.aiqin.mgs.order.api.domain.request.ReportAreaReturnSituationVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ReportCategoryGoodsDao {

    int deleteByType(Integer type);

    List<ReportCategoryGoods> selectList(ReportAreaReturnSituationVo vo);

    int insertBatch(@Param("records") List<ReportCategoryGoods> records);

}