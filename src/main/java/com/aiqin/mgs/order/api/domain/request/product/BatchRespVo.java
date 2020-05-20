package com.aiqin.mgs.order.api.domain.request.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Date;

public class BatchRespVo {
    @ApiModelProperty("批次号")
    @JsonProperty(value = "batch_code")
    private String batchCode;


    @ApiModelProperty("批次唯一标识")
    @JsonProperty(value = "batch_info_code")
    private String batchInfoCode;


    @ApiModelProperty("批次日期")
    @JsonProperty(value = "batch_date")
    private Date batchDate;


    @ApiModelProperty("批次对应库房编号")
    @JsonProperty(value = "warehouse_code")
    private String warehouseCode;


    @ApiModelProperty("批次库存")
    @JsonProperty(value = "batch_num")
    private Long batchNum;


    @ApiModelProperty("批次价格")
    @JsonProperty("batch_price")
    private BigDecimal batchPrice=new BigDecimal("0");
}
