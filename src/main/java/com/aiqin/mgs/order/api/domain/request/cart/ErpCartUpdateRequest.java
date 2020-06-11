package com.aiqin.mgs.order.api.domain.request.cart;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel("购物车修改请求参数")
public class ErpCartUpdateRequest {

    @ApiModelProperty(value = "购物车唯一标识 必填")
    @JsonProperty("cart_id")
    private String cartId;

    @ApiModelProperty(value = "数量 必填 不修改传原来的值")
    @JsonProperty("amount")
    private Integer amount;

    @ApiModelProperty(value = "活动id 不修改传原来的值")
    @JsonProperty("activity_id")
    private String activityId;

    @ApiModelProperty(value = "选中状态：0、不选 1、选中 必填，不修改传原来的值")
    @JsonProperty("line_check_status")
    private Integer lineCheckStatus;

    @ApiModelProperty(value = "商品价格 选填")
    @JsonProperty("price")
    private BigDecimal price;

}
