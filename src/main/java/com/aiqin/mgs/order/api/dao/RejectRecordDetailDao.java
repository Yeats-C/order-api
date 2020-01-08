package com.aiqin.mgs.order.api.dao;

import com.aiqin.mgs.order.api.domain.RejectRecordDetail;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RejectRecordDetailDao {
    int deleteByPrimaryKey(Long id);

    int insert(List<RejectRecordDetail> record);

    int insertSelective(RejectRecordDetail record);

    RejectRecordDetail selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(RejectRecordDetail record);

    int updateByPrimaryKey(RejectRecordDetail record);

    List<RejectRecordDetail> selectByRejectRecordCode(@Param("rejectRecordCode") String rejectRecordCode);
}