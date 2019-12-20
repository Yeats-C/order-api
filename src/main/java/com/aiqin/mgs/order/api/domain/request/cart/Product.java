package com.aiqin.mgs.order.api.domain.request.cart;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class Product {

    /***商品数量*/
    @ApiModelProperty(value = "商品数量")
    @JsonProperty("amount")
    private Integer amount;

    /***sku码*/
    @ApiModelProperty(value = "sku码")
    @JsonProperty("sku_id")
    private String skuId;
}
