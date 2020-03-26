package com.aiqin.mgs.order.api.dao;

import com.aiqin.mgs.order.api.domain.RejectRecord;
import com.aiqin.mgs.order.api.domain.request.RejectRequest;
import com.aiqin.mgs.order.api.domain.request.purchase.RejectQueryRequest;
import com.aiqin.mgs.order.api.domain.response.purchase.RejectRecordInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RejectRecordDao {
    int deleteByPrimaryKey(Long id);

    int insert(RejectRecord record);

    int updateByPrimaryKeySelective(RejectRecord record);

    int updateByPrimaryKey(RejectRecord record);

    /**
     * 根据ERP退货单生成爱亲退供单
     * @param record
     * @return
     */
    int insertSelective(RejectRecord record);

    /**
     * 修改退货单
     * @param rejectRecord
     * @return
     */
    int updateByReturnOrderCode(RejectRecord rejectRecord);

    RejectRecord selectByPrimaryKey(Long id);

    List<RejectRecord> selectByRequest(RejectRequest rejectRequest);

    RejectRecord selectByRejectRecordCode(@Param("rejectRecordCode")String rejectRecordCode);

    List<RejectRecordInfo> list(RejectQueryRequest rejectApplyQueryRequest);

    Integer listCount(RejectQueryRequest rejectApplyQueryRequest);

    RejectRecordInfo selectByRejectCode(String rejectRecordCode);
}