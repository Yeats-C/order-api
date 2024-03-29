package com.aiqin.mgs.order.api.domain.response.market;

import com.aiqin.mgs.order.api.domain.OrderInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

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

    @ApiModelProperty(value = "订单数")
    @JsonProperty("order_count")
    private Long orderCount;

    @ApiModelProperty(value = "会员订单数")
    @JsonProperty("member_order_count")
    private Long memberOderCount;

    @ApiModelProperty(value = "销售额")
    @JsonProperty("sale_amount")
    private Long saleAmount;

    @ApiModelProperty(value = "成本额")
    @JsonProperty("cost_amount")
    private Long costAmount;

    @JsonProperty("order_code")
    @ApiModelProperty("订单编号集合")
    private List<OrderInfo> orderCode;


}
