package com.aiqin.mgs.order.api.service;

import com.aiqin.ground.util.protocol.http.HttpResponse;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

public interface StoreMonthService {

    //根据门店名称或门店编码查询销量
    HttpResponse selectStoreMonth(String storeName);

    HttpResponse selectStoreMonths(@Param("storeAll") List<String> storeAll);
}
