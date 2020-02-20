package com.aiqin.mgs.order.api.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@ApiModel("门店补货列表统计detail报表")
public class ReportStoreGoodsDetail {

    @ApiModelProperty(value = "id")
    @JsonProperty("id")
    private Long id;

    @ApiModelProperty(value = "sku编码")
    @JsonProperty("sku_code")
    private String skuCode;

    @ApiModelProperty(value = "sku名称")
    @JsonProperty("sku_name")
    private String skuName;

    @ApiModelProperty(value = "门店编码")
    @JsonProperty("store_code")
    private String storeCode;

    @ApiModelProperty(value = "门店名称")
    @JsonProperty("store_name")
    private String storeName;

    @ApiModelProperty(value = "品牌名称")
    @JsonProperty("brand_id")
    private String brandId;

    @ApiModelProperty(value = "品牌名称")
    @JsonProperty("brand_name")
    private String brandName;

    @ApiModelProperty(value = "商品数量")
    @JsonProperty("num")
    private Long num;

    @ApiModelProperty(value = "商品总金额")
    @JsonProperty("amount")
    private BigDecimal amount;

    @ApiModelProperty(value = "统计月份")
    @JsonProperty("count_time")
    private String countTime;

    @ApiModelProperty(value = "创建时间")
    @JsonProperty("create_time")
    private Date createTime;

}