package com.aiqin.mgs.order.api.service.impl;

import com.aiqin.mgs.order.api.domain.CartOrderInfo;
import com.aiqin.mgs.order.api.service.CartOrderService;

import java.util.ArrayList;
import java.util.List;

public class CartOrderServiceImpl implements CartOrderService {

    private List<CartOrderInfo> items = new ArrayList<>(10);

    @Override
    public void addCartInfo(CartOrderInfo cartOrderInfo) {

    }
}
