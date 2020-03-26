package com.aiqin.mgs.order.api.domain.request.cart;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("购物车删除请求参数")
public class ErpCartDeleteRequest {

    @ApiModelProperty(value = "购物车行唯一标识")
    @JsonProperty("cart_id")
    private String cartId;
}
