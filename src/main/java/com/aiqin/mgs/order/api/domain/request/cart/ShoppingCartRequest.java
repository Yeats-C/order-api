package com.aiqin.mgs.order.api.domain.request.cart;

import lombok.Data;

import java.util.List;

@Data
public class ShoppingCartRequest {

    /***商品id*/
    private String productId;

    /**商品集合*/
    private List<Product> products;

    /***门店id*/
    private String storeId;

    /***创建来源*/
    private String createSource;


}
