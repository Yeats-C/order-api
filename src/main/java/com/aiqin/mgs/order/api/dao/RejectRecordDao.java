package com.aiqin.mgs.order.api.dao;

import com.aiqin.mgs.order.api.domain.RejectRecord;
import com.aiqin.mgs.order.api.domain.request.RejectRequest;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RejectRecordDao {
    int deleteByPrimaryKey(Long id);

    int insert(RejectRecord record);

    /**
     * 根据ERP退货单生成爱亲退供单
     * @param record
     * @return
     */
    int insertSelective(RejectRecord record);

    RejectRecord selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(RejectRecord record);

    int updateByPrimaryKey(RejectRecord record);

    List<RejectRecord> selectByRequest(RejectRequest rejectRequest);

    RejectRecord selectByRejectRecordCode(@Param("rejectRecordCode")String rejectRecordCode);
}