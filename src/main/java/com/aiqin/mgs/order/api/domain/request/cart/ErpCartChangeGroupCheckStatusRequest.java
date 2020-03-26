package com.aiqin.mgs.order.api.domain.request.cart;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("购物车全选全不选请求参数")
public class ErpCartChangeGroupCheckStatusRequest {

    @ApiModelProperty(value = "门店Id")
    @JsonProperty("store_id")
    private String storeId;

    @ApiModelProperty(value = "订单类型 1直送 2配送")
    @JsonProperty("product_type")
    private Integer productType;

    @ApiModelProperty(value = "选中状态 0全不选 1全选")
    @JsonProperty("line_check_status")
    private Integer lineCheckStatus;
}
