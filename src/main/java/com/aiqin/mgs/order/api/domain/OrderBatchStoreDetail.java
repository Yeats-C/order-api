package com.aiqin.mgs.order.api.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
@Data
public class OrderBatchStoreDetail implements Serializable {
    //订单批次明细（仓卡）
    @ApiModelProperty(value = "行号")
    @JsonProperty("line_code")
    @NotEmpty(message = "行号不能为空")
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

    @ApiModelProperty(value="批次编码")
    @JsonProperty("batch_info_code")
    @NotEmpty(message = "批次编码（唯一标识）不能为空")
    private String batchInfoCode;

    @ApiModelProperty("销售数量")
    @JsonProperty("total_count")
    private Long totalCount;

    @ApiModelProperty("实际销售数量")
    @JsonProperty("actual_total_count")
    private Long actualTotalCount;

    @ApiModelProperty("传入库房编码:1:销售库，2:特卖库")
    private String warehouseTypeCode;

    @ApiModelProperty("批次价格")
    @JsonProperty("batch_price")
    private BigDecimal batchPrice=new BigDecimal("0");
}
