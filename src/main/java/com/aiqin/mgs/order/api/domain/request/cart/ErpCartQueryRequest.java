package com.aiqin.mgs.order.api.domain.request.cart;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
@ApiModel("购物车查询请求参数")
public class ErpCartQueryRequest {

    @ApiModelProperty(value = "门店Id")
    @JsonProperty("store_id")
    @NotEmpty(message = "门店id不能为空")
    private String storeId;

    @ApiModelProperty(value = "订单类型 1直送 2配送")
    @JsonProperty("product_type")
    private Integer productType;
}
