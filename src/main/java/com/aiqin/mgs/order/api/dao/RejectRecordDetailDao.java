package com.aiqin.mgs.order.api.dao;

import com.aiqin.mgs.order.api.domain.RejectRecordDetail;

import java.util.List;

public interface RejectRecordDetailDao {
    int deleteByPrimaryKey(Long id);

    int insert(List<RejectRecordDetail> record);

    /**
     * 根据ERP退货单生成爱亲退供单详情
     * @param record
     * @return
     */
    int insertSelective(RejectRecordDetail record);

    RejectRecordDetail selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(RejectRecordDetail record);

    int updateByPrimaryKey(RejectRecordDetail record);
}