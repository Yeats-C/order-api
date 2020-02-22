package com.aiqin.mgs.order.api.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@ApiModel("门店补货报表")
public class ReportStoreGoods implements Serializable {

    @ApiModelProperty(value = "门店名称")
    @JsonProperty("id")
    private Long id;

    @ApiModelProperty(value = "门店名称")
    @JsonProperty("store_name")
    private String storeName;

    @ApiModelProperty(value = "门店id")
    @JsonProperty("store_id")
    private String storeId;

    @ApiModelProperty(value = "门店编码")
    @JsonProperty("store_code")
    private String storeCode;

    @ApiModelProperty(value = "品牌id")
    @JsonProperty("brand_id")
    private String brandId;

    @ApiModelProperty(value = "品牌名称")
    @JsonProperty("brand_name")
    private String brandName;

    @ApiModelProperty(value = "数量")
    @JsonProperty("num")
    private Long num;

    @ApiModelProperty(value = "金额")
    @JsonProperty("amount")
    private BigDecimal amount;

    @ApiModelProperty(value = "同比")
    @JsonProperty("tong_ratio")
    private BigDecimal tongRatio;

    @ApiModelProperty(value = "环比")
    @JsonProperty("chain_ratio")
    private BigDecimal chainRatio;

    @ApiModelProperty(value = "统计时间")
    @JsonProperty("count_time")
    private String countTime;

    @ApiModelProperty(value = "创建时间")
    @JsonProperty("create_time")
    private Date createTime;

    @ApiModelProperty(value = "总销量")
    @JsonProperty("total_num")
    private Long totalNum;

    @ApiModelProperty(value = "总额")
    @JsonProperty("total_amount")
    private BigDecimal totalAmount;

    @ApiModelProperty(value = "总的同比")
    @JsonProperty("total_tong_ratio")
    private BigDecimal totalTongRatio;

    @ApiModelProperty(value = "总的环比")
    @JsonProperty("total_chain_ratio")
    private BigDecimal totalChainRatio;

}