package com.aiqin.mgs.order.api.service;

import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.domain.request.cart.ShoppingCartRequest;


public interface CartOrderService {

    //添加商品到购物车
    HttpResponse addCart(ShoppingCartRequest shoppingCartRequest);

    //根据门店id查询购物车
    HttpResponse selectCartByStoreId(String storeId,Integer productType,String skuId,Integer lineCheckStatus);

    //返回购物车中的所有商品数量
    HttpResponse<Integer> getTotal(String storeId);

    //清空购物车或者删除购物车中的单条商品
    HttpResponse deleteCartInfo(String storeId,String skuId,Integer lineCheckStatus);

}
