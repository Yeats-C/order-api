package com.aiqin.mgs.order.api.dao;

import com.aiqin.mgs.order.api.domain.RejectRecordDetailBatch;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RejectRecordDetailBatchDao {
    int deleteByPrimaryKey(Long id);

    int insert(RejectRecordDetailBatch record);

    int insertSelective(RejectRecordDetailBatch record);

    RejectRecordDetailBatch selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(RejectRecordDetailBatch record);

    int updateByPrimaryKey(RejectRecordDetailBatch record);

    List<RejectRecordDetailBatch> selectByRejectRecordCode(@Param("rejectRecordCode")String rejectRecordCode);
}