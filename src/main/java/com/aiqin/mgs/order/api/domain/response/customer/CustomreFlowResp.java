package com.aiqin.mgs.order.api.domain.response.customer;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;


/**
 * @author ch
 * Date: 2019/12/12 10:00
 * Description:
 */
@Data
@ApiModel("客流封装返回对象resp")
public class CustomreFlowResp {

    @ApiModelProperty("客流日流量")
    @JsonProperty("customre_flow_dailyList")
    private List<CustomreFlowDaily> customreFlowDailyList;

    @ApiModelProperty("客流月流量")
    @JsonProperty("customre_flow_year_monthList")
    private List<CustomreFlowYearMonth> customreFlowYearMonthList;
}
