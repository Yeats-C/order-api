package com.aiqin.mgs.order.api.domain.response.statistical;

import com.aiqin.mgs.order.api.domain.statistical.BusinessStatistical;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * Createed by sunx on 2019/4/4.<br/>
 */
@Data
@ApiModel("营业数据统计返回实体")
public class BusinessStatisticalResponse {

    @JsonProperty("yesterday_price")
    @ApiModelProperty("昨日订单实收金额")
    private Long yesterdayPrice;

    @JsonProperty("yesterday_amount")
    @ApiModelProperty("昨日订单数")
    private Integer yesterdayAmount;

    @JsonProperty("yesterday_avg_price")
    @ApiModelProperty("昨日人均消费")
    private Long yesterdayAvgPrice;

    @JsonProperty("today_amount")
    @ApiModelProperty("今日订单数")
    private Integer todayAmount;

    @JsonProperty("month_data")
    @ApiModelProperty("月度数据，当年")
    private List<BusinessStatistical> monthData;
}
