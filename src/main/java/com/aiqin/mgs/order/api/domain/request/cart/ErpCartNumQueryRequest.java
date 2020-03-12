package com.aiqin.mgs.order.api.domain.request.cart;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
@ApiModel("查询购物车商品数量请求参数")
public class ErpCartNumQueryRequest {

    @ApiModelProperty(value = "门店Id")
    @JsonProperty("store_id")
    @NotEmpty(message = "门店id不能为空")
    private String storeId;

    @ApiModelProperty(value = "订单类型 1、只查直送 2、只查配送")
    @JsonProperty("product_type")
    private Integer productType;

    @ApiModelProperty(value = "勾选状态 0、只查未勾选的 1、只查询勾选的  erp端查询必须传1")
    @JsonProperty("line_check_status")
    private Integer lineCheckStatus;
}
