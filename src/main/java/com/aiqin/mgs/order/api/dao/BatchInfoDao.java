package com.aiqin.mgs.order.api.dao;

import com.aiqin.mgs.order.api.domain.BatchInfo;
import com.aiqin.mgs.order.api.domain.ReturnOrderDetailBatch;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BatchInfoDao {
    int deleteByPrimaryKey(Long id);

    int deleteByBasicId(String basicId);

    int insertSelective(ReturnOrderDetailBatch record);

    ReturnOrderDetailBatch selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ReturnOrderDetailBatch record);

    int insertBatchInfo(@Param("barchInfoList") List<BatchInfo> barchInfoList);

    List<BatchInfo> selectBatchInfoList(BatchInfo batchInfo);
}