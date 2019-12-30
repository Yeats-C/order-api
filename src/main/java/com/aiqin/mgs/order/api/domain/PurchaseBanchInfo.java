package com.aiqin.mgs.order.api.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
@Data
public class PurchaseBanchInfo implements Serializable {
    @ApiModelProperty(value = "行号")
    private Long lineCode;
    @ApiModelProperty(value = "SKU编码")
    private String skuCode;
    @ApiModelProperty(value = "SKU名称")
    private String skuName;
    @ApiModelProperty(value = "批次编号")
    private String batchCode;
    @ApiModelProperty(value = "生产日期")
    private Date productDate;
    @ApiModelProperty(value = "实际销售数量")
    private Long actualToalCount;
}
