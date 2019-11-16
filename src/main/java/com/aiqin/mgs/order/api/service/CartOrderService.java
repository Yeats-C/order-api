package com.aiqin.mgs.order.api.service;

import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.domain.CartOrderInfo;

import javax.validation.Valid;
import java.util.List;


public interface CartOrderService {

    //添加商品到购物车
    HttpResponse addCartInfo(CartOrderInfo cartOrderInfo);

    //根据门店id查询购物车
    HttpResponse selectCartByDistributorId(String distributorId,Integer distributionType);

    //全部清空购物车
    HttpResponse deleteCartInfo(String distributorId);

    //删除购物车中的单件商品
    HttpResponse deleteCartInfoById(List<String> skuCodes, String distributorId);

    //更新购物车清单
    HttpResponse updateCartByMemberId(@Valid CartOrderInfo cartOrderInfo);

}
