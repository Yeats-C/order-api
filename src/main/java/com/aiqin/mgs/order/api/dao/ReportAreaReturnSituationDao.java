package com.aiqin.mgs.order.api.dao;

import com.aiqin.mgs.order.api.domain.ReportAreaReturnSituation;
import com.aiqin.mgs.order.api.domain.request.ReportAreaReturnSituationVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ReportAreaReturnSituationDao {

    int deleteByProvinceAndType(ReportAreaReturnSituationVo vo);

    int deleteByType(ReportAreaReturnSituationVo vo);

    int insert(ReportAreaReturnSituation record);

    int insertSelective(ReportAreaReturnSituation record);

    ReportAreaReturnSituation selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ReportAreaReturnSituation record);

    int updateByPrimaryKey(ReportAreaReturnSituation record);

    List<ReportAreaReturnSituation> selectList(ReportAreaReturnSituationVo vo);

    int insertBatch(@Param("records") List<ReportAreaReturnSituation> records);

    ReportAreaReturnSituation selectOrderAmountByStoreCodes(ReportAreaReturnSituationVo vo);

    ReportAreaReturnSituation selectOrderCountByStoreCodes(ReportAreaReturnSituationVo vo);

    List<ReportAreaReturnSituation> topProvinceAmount(@Param("type") Integer type);

}