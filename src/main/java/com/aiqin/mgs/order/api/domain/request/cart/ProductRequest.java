package com.aiqin.mgs.order.api.domain.request.cart;

import lombok.Data;

import java.util.List;

@Data
public class ProductRequest {

    private String productId;

    private String storeId;

    private List<String> skuIds;
}
