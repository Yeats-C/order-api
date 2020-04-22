package com.aiqin.mgs.order.api.dao;

import com.aiqin.mgs.order.api.domain.OrderGiveApproval;

public interface OrderGiveApprovalDao {

    int deleteByPrimaryKey(Long id);

    int insert(OrderGiveApproval record);

    int insertSelective(OrderGiveApproval record);

    OrderGiveApproval selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OrderGiveApproval record);

    int updateByFormNoSelective(OrderGiveApproval record);

    int updateByPrimaryKey(OrderGiveApproval record);

    OrderGiveApproval selectByFormNo(String id);

}