package com.aiqin.mgs.order.api.domain.response.market;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author ch
 * @Description
 * @Date 2020/3/19 11:47
 */
@Data
@ApiModel("活动数据报表中每天的订单情况Resp")
public class ActivityReportOrderResp {

    @ApiModelProperty(value = "时间月日")
    @JsonProperty("months_day")
    private String monthsDay;

    @ApiModelProperty(value = "会员状态 （0 非会员，1 会员）")
    @JsonProperty("member_status")
    private Integer memberStatus;

    @ApiModelProperty(value = "订单数")
    @JsonProperty("order_count")
    private Long orderCount;

    @ApiModelProperty(value = "销售额")
    @JsonProperty("sale_amount")
    private Long saleAmount;

}
