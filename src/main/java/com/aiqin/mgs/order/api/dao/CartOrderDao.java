package com.aiqin.mgs.order.api.dao;

import com.aiqin.mgs.order.api.domain.CartOrderInfo;
import org.apache.ibatis.annotations.Param;

import javax.validation.Valid;
import java.util.List;

public interface CartOrderDao {

    //添加商品到购物车
    void insertCart(@Valid CartOrderInfo cartOrderInfo) throws Exception;

    //根据门店id查询购物车
    List<CartOrderInfo> selectCartByStoreId(@Valid CartOrderInfo cartOrderInfo) throws Exception;


    //调用购物车修改逻辑
    void updateCartById(@Valid CartOrderInfo cartOrderInfo) throws Exception;

    //判断SKU商品是否已存在购物车中
    String isYesCart(@Valid CartOrderInfo cartOrderInfo) throws Exception;

    //计算添加购物车中商品的总数量
    Integer getTotal(@Param("storeId") String storeId) throws Exception;

    // 删除购物车中商品
    void deleteCart(@Param("storeId") String storeId,@Param("skuId") String skuId,@Param("lineCheckStatus") Integer lineCheckStatus) throws Exception;



}
