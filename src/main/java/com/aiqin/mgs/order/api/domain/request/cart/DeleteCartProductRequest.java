package com.aiqin.mgs.order.api.domain.request.cart;

import lombok.Data;

@Data
public class DeleteCartProductRequest {

    /**门店id*/
    private String storeId;

    /**skuId*/
    private String skuId;

    /**删除勾选商品*/
    private Integer lineCheckStatus;
}
