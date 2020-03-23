package com.aiqin.mgs.order.api.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author csf
 */
@Data
@ApiModel("促销活动详情-销售数据-统计分析bean")
public class ActivitySales implements Serializable {


    /**活动相关订单销售额(当订单中的商品命中了这个促销活动时，这个订单纳入统计，统计主订单。)*/
    @ApiModelProperty(value = "活动相关订单销售额(当订单中的商品命中了这个促销活动时，这个订单纳入统计，统计主订单。)")
    @JsonProperty("activitySales")
    private BigDecimal activitySales;

    /**活动订单数(当订单中的商品命中了这个促销活动时，这个订单纳入统计，统计主订单。)*/
    @ApiModelProperty(value = "活动订单数(当订单中的商品命中了这个促销活动时，这个订单纳入统计，统计主订单。")
    @JsonProperty("activitySalesNum")
    private BigDecimal activitySalesNum;

    /**活动商品销售额 */
    @ApiModelProperty(value = "活动商品销售额")
    @JsonProperty("productSales")
    private BigDecimal productSales;

    /**补货门店数 */
    @ApiModelProperty(value = "补货门店数")
    @JsonProperty("storeNum")
    private Integer storeNum;

    /**平均单价 */
    @ApiModelProperty(value = "平均单价")
    @JsonProperty("averageUnitPrice")
    private BigDecimal averageUnitPrice;

    /**活动名称*/
    @ApiModelProperty(value = "活动名称")
    @JsonProperty("activity_name")
    private String activityName="";

}
