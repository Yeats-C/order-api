package com.aiqin.mgs.order.api.dao;

import com.aiqin.mgs.order.api.domain.RejectRecordDetailBatch;

public interface RejectRecordDetailBatchDao {
    int deleteByPrimaryKey(Long id);

    int insert(RejectRecordDetailBatch record);

    int insertSelective(RejectRecordDetailBatch record);

    RejectRecordDetailBatch selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(RejectRecordDetailBatch record);

    int updateByPrimaryKey(RejectRecordDetailBatch record);
}