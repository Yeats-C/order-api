package com.aiqin.mgs.order.api.dao;

import com.aiqin.mgs.order.api.domain.OrderStoreDetailBatch;

import java.util.List;

public interface OrderStoreDetailBatchDao {
    int deleteByPrimaryKey(Long id);

    int insert(OrderStoreDetailBatch record);

    int insertSelective(OrderStoreDetailBatch record);

    OrderStoreDetailBatch selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OrderStoreDetailBatch record);

    int updateByPrimaryKey(OrderStoreDetailBatch record);

    List<OrderStoreDetailBatch> select(OrderStoreDetailBatch orderStoreDetailBatch);
}