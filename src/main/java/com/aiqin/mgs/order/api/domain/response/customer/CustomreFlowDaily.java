package com.aiqin.mgs.order.api.domain.response.customer;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * @author ch
 * Date: 2019/12/12 10:00
 * Description:
 */
@Data
@ApiModel("客流年月日resp")
public class CustomreFlowDaily {

    @ApiModelProperty("主键id")
    @JsonProperty("id")
    private Integer id;

    @ApiModelProperty("年月日")
    @JsonProperty("stat_year_month_day")
    private String statYearMonthDay;

    @ApiModelProperty("年")
    @JsonProperty("stat_year")
    private String statYear;

    @ApiModelProperty("月")
    @JsonProperty("stat_month")
    private String statMonth;

    @ApiModelProperty("日")
    @JsonProperty("stat_day")
    private String statDay;

     @ApiModelProperty("门店id")
    @JsonProperty("store_id")
    private String storeId;

    @ApiModelProperty("门店名称")
    @JsonProperty("store_name")
    private String storeName;

    @ApiModelProperty("客流人流量")
    @JsonProperty("total_customer_flow")
    private Integer totalCustomerFlow;

    @ApiModelProperty("目标人流量")
    @JsonProperty("goal_num")
    private Integer goalNum;

    @ApiModelProperty("客流差值")
    @JsonProperty("different_num")
    private Integer differentNum;


}
