package com.aiqin.mgs.order.api.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("子单拆单数量表")
public class OrderSplitsNum {

    @ApiModelProperty(value = "id")
    @JsonProperty("id")
    private Long id;

    @ApiModelProperty(value = "主订单编码")
    @JsonProperty("order_code")
    private String orderCode;

    @ApiModelProperty(value = "拆单数量")
    @JsonProperty("num")
    private Integer num;

    @ApiModelProperty(value = "是否发货完成 0:未完成 1:已完成")
    @JsonProperty("status")
    private Integer status;

}