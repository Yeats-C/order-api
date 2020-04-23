package com.aiqin.mgs.order.api.service;

import com.aiqin.ground.util.protocol.http.HttpResponse;

public interface StoreMonthService {

    //根据门店名称或门店编码查询销量
    HttpResponse selectStoreMonth(String storeName);
}
