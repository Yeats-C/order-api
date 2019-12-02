package com.aiqin.mgs.order.api.domain.request.cart;

import lombok.Data;

@Data
public class Product {

    /***商品数量*/
    private Integer amount;

    /***sku码*/
    private String skuId;
}
