package com.aiqin.mgs.order.api.service;

import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.domain.AuthToken;
import com.aiqin.mgs.order.api.domain.CartOrderInfo;
import com.aiqin.mgs.order.api.domain.request.cart.ShoppingCartRequest;
import com.aiqin.mgs.order.api.domain.response.cart.StoreCartProductResponse;

import java.util.List;


public interface CartOrderService {

    /**
     * 添加商品到购物车
     * @param shoppingCartRequest 请求参数
     * @return
     */
    HttpResponse addCart(ShoppingCartRequest shoppingCartRequest, AuthToken authToken);

    /**
     * 根据门店id查询购物车
     * @param storeId 门店id
     * @param productType 商品类型 1：配送 2：直送 3：货架
     * @param skuId sku码
     * @param lineCheckStatus 勾选标记
     * @param number 商品数量
     * @return
     */
    HttpResponse selectCartByStoreId(String storeId,Integer productType,String skuId,Integer lineCheckStatus,Integer number);

    /**
     * 更新和查询购物车
     *
     * @param storeId         门店id
     * @param productType     商品类型
     * @param skuId           sku编码
     * @param lineCheckStatus 勾选标记
     * @param number          数量
     * @param activityId      活动id
     * @return com.aiqin.ground.util.protocol.http.HttpResponse
     */
    HttpResponse queryCartByStoreId(String storeId, Integer productType, String skuId, Integer lineCheckStatus, Integer number, String activityId);

    /**
     * 返回购物车中的所有商品数量
     * @param storeId 门店ID
     * @return
     */
    HttpResponse<Integer> getTotal(String storeId);

    /**
     * 清空购物车或者删除购物车中的单条商品
     * @param storeId 门店id
     * @param skuId skuID
     * @param lineCheckStatus 勾选标记
     * @param productType 订单类型 1直送 2配送 3货架
     * @return
     */
    HttpResponse deleteCartInfo(String storeId, String skuId, Integer lineCheckStatus, Integer productType);

    /**
     * 显示购物车中的勾选商品
     * @param cartOrderInfo 购物车中的商品信息
     * @return
     */
    HttpResponse<StoreCartProductResponse> displayCartLineCheckProduct(CartOrderInfo cartOrderInfo);

}
