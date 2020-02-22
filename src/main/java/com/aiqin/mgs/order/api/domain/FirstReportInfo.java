package com.aiqin.mgs.order.api.domain;


import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@ApiModel("首单报表对象")
public class FirstReportInfo {

    @ApiModelProperty(value = "经营区域")
    @JsonProperty("copartner_area_id")
    private String copartnerAreaId;

    @ApiModelProperty(value = "经营区域")
    @JsonProperty("copartner_area_name")
    private String copartnerAreaName;

    @ApiModelProperty(value = "首单直送销售金额")
    @JsonProperty("zs_sales_amount")
    private BigDecimal zsSalesAmount;

    @ApiModelProperty(value = "首单直送同期")
    @JsonProperty("zs_same_period")
    private int zsSamePeriod;

    @ApiModelProperty(value = "首单直送上期")
    @JsonProperty("zs_on_period")
    private int zsOnPeriod;

    @ApiModelProperty(value = "首单直送同比")
    @JsonProperty("zs_same_ratio")
    private int zsSameRatio;

    @ApiModelProperty(value = "首单直送环比")
    @JsonProperty("zs_ring_ratio")
    private int zsRingRatio;

    @ApiModelProperty(value = "首单配送销售金额")
    @JsonProperty("ps_sales_amount")
    private BigDecimal psSalesAmount;

    @ApiModelProperty(value = "首单配送同期")
    @JsonProperty("ps_same_period")
    private int psSamePeriod;

    @ApiModelProperty(value = "首单配送上期")
    @JsonProperty("ps_on_period")
    private int psOnPeriod;

    @ApiModelProperty(value = "首单配送同比")
    @JsonProperty("ps_same_ratio")
    private int psSameRatio;

    @ApiModelProperty(value = "首单配送环比")
    @JsonProperty("ps_ring_ratio")
    private int psRingRatio;

    @ApiModelProperty(value = "首单货架销售金额")
    @JsonProperty("hj_sales_amount")
    private BigDecimal hjSalesAmount;

    @ApiModelProperty(value = "首单货架同期")
    @JsonProperty("hj_same_period")
    private int hjSamePeriod;

    @ApiModelProperty(value = "首单货架上期")
    @JsonProperty("hj_on_period")
    private int hjOnPeriod;

    @ApiModelProperty(value = "首单货架同比")
    @JsonProperty("hj_same_ratio")
    private int hjSameRatio;

    @ApiModelProperty(value = "首单货架环比")
    @JsonProperty("hj_ring_ratio")
    private int hjRingRatio;

    @ApiModelProperty(value = "创建时间")
    @JsonProperty("create_time")
    private Date createTime;

    @ApiModelProperty(value = "统计月份")
    @JsonProperty("report_time")
    private String reportTime;

 }
