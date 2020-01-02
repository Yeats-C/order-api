package com.aiqin.mgs.order.api.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
@Data
public class OrderStoreDelete implements Serializable {
    //订单商品明细行
    @ApiModelProperty(value = "行号")
    private Long lineCode;
    @ApiModelProperty(value = "实发数量")
    private Long actualProductCount;
    @ApiModelProperty(value = "SKU编码")
    private String skuCode;
    @ApiModelProperty(value = "SKU名称")
    private String skuName;
}
