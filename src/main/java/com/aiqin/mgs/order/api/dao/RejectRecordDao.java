package com.aiqin.mgs.order.api.dao;

import com.aiqin.mgs.order.api.domain.RejectRecord;

public interface RejectRecordDao {
    int deleteByPrimaryKey(Long id);

    int insert(RejectRecord record);

    RejectRecord selectByPrimaryKey(Long id);

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
}