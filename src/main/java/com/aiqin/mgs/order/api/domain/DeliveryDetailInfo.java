package com.aiqin.mgs.order.api.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.math.BigDecimal;
@Data
public class DeliveryDetailInfo implements Serializable {
    @ApiModelProperty(value = "销售单单号")
    @JsonProperty("order_code")
    @NotEmpty(message = "销售单单号不能为空")
    private String orderCode;

    @ApiModelProperty(value = "销售单运费")
    @JsonProperty("transport_amount")
    private BigDecimal transportAmount;
}
