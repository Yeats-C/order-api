package com.aiqin.mgs.order.api.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
@Data
public class RejectRecordDetailInfo implements Serializable {
    //退单明细
    @ApiModelProperty(value = "行号")
    private Long lineCode;
    @ApiModelProperty(value = "SKU编码")
    private String skuCode;
    @ApiModelProperty(value = "SKU名称")
    private String skuName;
    @ApiModelProperty(value = "实际拖货数量")
    private Long autualTotalCode;
}
