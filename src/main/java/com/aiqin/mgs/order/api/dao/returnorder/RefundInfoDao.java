package com.aiqin.mgs.order.api.dao.returnorder;

import com.aiqin.mgs.order.api.domain.RefundInfo;

public interface RefundInfoDao {

    int deleteByPrimaryKey(Integer id);

    int insertSelective(RefundInfo record);

    RefundInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RefundInfo record);

    /**
     * 根据订单编码修改支付流水信息
     * @param record
     * @return
     */
    int updateByOrderCode(RefundInfo record);

}