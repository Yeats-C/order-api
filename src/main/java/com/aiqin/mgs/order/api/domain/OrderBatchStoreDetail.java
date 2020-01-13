package com.aiqin.mgs.order.api.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Date;
@Data
public class OrderBatchStoreDetail implements Serializable {
    //订单批次明细（仓卡）
    @ApiModelProperty(value = "行号")
    @JsonProperty("line_code")

    private Long lineCode;
    @ApiModelProperty(value = "SKU编码")
    @JsonProperty("sku_code")
    @NotEmpty(message = "SKU编码不能为空")
    private String skuCode;

    @ApiModelProperty(value = "SKU名称")
    @JsonProperty("sku_name")
    @NotEmpty(message = "SKU名称不能为空")
    private String skuName;

    @ApiModelProperty(value = "批次编号")
    @JsonProperty("batch_code")
    @NotEmpty(message = "批次编号不能为空")
    private String batchCode;

    @ApiModelProperty(value = "生产日期",example = "2001-01-01 01:01:01")
    @JsonProperty("product_date")
    private Date productDate;
}
