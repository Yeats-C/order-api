package com.aiqin.mgs.order.api.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
@Data
public class PurchaseDetailInfo implements Serializable {
    @ApiModelProperty(value = "行号")
    private Long lineCode;
    @ApiModelProperty(value = "SKU编码")
    private String skuCode;
    @ApiModelProperty(value = "SKU名称")
    private String skuName;
    @ApiModelProperty(value = "实际销售数量")
    private Long actualToalCount;
}
