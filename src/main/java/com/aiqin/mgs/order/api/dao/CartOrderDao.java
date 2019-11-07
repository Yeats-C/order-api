package com.aiqin.mgs.order.api.dao;

import com.aiqin.mgs.order.api.domain.CartOrderInfo;

import javax.validation.Valid;

public interface CartOrderDao {

    //购物车商品项 0:添加 1:修改....
    void insertCart(@Valid CartOrderInfo cartOrderInfo) throws Exception;

}
