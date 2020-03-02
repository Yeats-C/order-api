package com.aiqin.mgs.order.api.dao;

import com.aiqin.mgs.order.api.domain.ReportAreaReturnSituation;

public interface ReportAreaReturnSituationDao {

    int deleteByPrimaryKey(Long id);

    int insert(ReportAreaReturnSituation record);

    int insertSelective(ReportAreaReturnSituation record);

    ReportAreaReturnSituation selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ReportAreaReturnSituation record);

    int updateByPrimaryKey(ReportAreaReturnSituation record);
}