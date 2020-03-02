package com.aiqin.mgs.order.api.dao;

import com.aiqin.mgs.order.api.domain.ReportAreaReturnSituation;
import com.aiqin.mgs.order.api.domain.request.ReportAreaReturnSituationVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ReportAreaReturnSituationDao {

    int deleteByPrimaryKey(Long id);

    int insert(ReportAreaReturnSituation record);

    int insertSelective(ReportAreaReturnSituation record);

    ReportAreaReturnSituation selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ReportAreaReturnSituation record);

    int updateByPrimaryKey(ReportAreaReturnSituation record);

    List<ReportAreaReturnSituation> selectList(ReportAreaReturnSituationVo vo);

    int insertBatch(@Param("records") List<ReportAreaReturnSituation> records);

}