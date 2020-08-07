package com.aiqin.mgs.order.api.domain.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author jinghaibo
 * Date: 2020/8/6 20:44
 * Description:
 */
@Data
public class OrderSumCountResponse {
    @ApiModelProperty("储值金额")
    @JsonProperty("recharge_amount")
    private Long rechargeAmount;
    @ApiModelProperty("充值笔数")
    @JsonProperty("recharge_order_amount")
    private Long rechargeOrderAmount;
}
