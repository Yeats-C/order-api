package com.aiqin.mgs.order.api.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
@Data
public class DeliverydetailInfo implements Serializable {
    @ApiModelProperty(value = "销售单单号")
    private String orderCode;
    @ApiModelProperty(value = "销售单运费")
    private BigDecimal transportAmount;
}
