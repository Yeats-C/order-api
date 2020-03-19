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

    @ApiModelProperty(value = "订单数")
    @JsonProperty("order_count")
    private Long orderCount;

    @ApiModelProperty(value = "会员订单数")
    @JsonProperty("member_order_count")
    private Long memberOrderCount;

    @ApiModelProperty(value = "销售额")
    @JsonProperty("sale_amount")
    private Long saleAmount;

}
