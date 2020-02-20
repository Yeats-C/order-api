package com.aiqin.mgs.order.api.domain.response.report;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * description: ReportStoreGoodsCountDetailResponse
 * date: 2020/2/20 9:10
 * author: hantao
 * version: 1.0
 */
@Data
@ApiModel("门店补货列表统计商品detail报表")
public class ReportStoreGoodsCountDetailResponse implements Serializable {

    @ApiModelProperty(value = "数量")
    private Long num;

    @ApiModelProperty(value = "sku编码")
    private String skuCode;

    @ApiModelProperty(value = "商品名称")
    private String skuName;

    @ApiModelProperty(value = "金额")
    private BigDecimal amount;

}
