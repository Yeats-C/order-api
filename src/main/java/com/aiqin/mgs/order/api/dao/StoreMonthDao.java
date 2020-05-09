package com.aiqin.mgs.order.api.dao;

import com.aiqin.mgs.order.api.domain.StoreInfo;
import com.aiqin.mgs.order.api.domain.StoreMonthResponse;

import java.util.List;

public interface StoreMonthDao {

    List<StoreMonthResponse> selectStoreByName(StoreInfo storeName);
}
