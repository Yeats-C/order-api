package com.aiqin.mgs.order.api.domain.request.cart;

import lombok.Data;

import java.util.List;

@Data
public class ShoppingCartRequest {

    /***商品id*/
    private String productId;

    /***商品数量*/
    private Integer amount;

    /***门店id*/
    private String storeId;

    /***sku码集合*/
    private List<String> skuIds;

}
