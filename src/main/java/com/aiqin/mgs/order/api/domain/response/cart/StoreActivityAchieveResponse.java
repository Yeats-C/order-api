package com.aiqin.mgs.order.api.domain.response.cart;

import com.aiqin.mgs.order.api.domain.Activity;
import com.aiqin.mgs.order.api.domain.ActivityRule;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel("查询门店活动满足情况返回值")
public class StoreActivityAchieveResponse {

    @ApiModelProperty(value = "活动头信息")
    @JsonProperty("activity")
    private Activity activity;

    @ApiModelProperty(value = "本品总金额")
    @JsonProperty("total_money")
    private BigDecimal totalMoney;

    @ApiModelProperty(value = "本品总数量")
    @JsonProperty("total_count")
    private Integer totalCount;

    @ApiModelProperty(value = "活动最低梯度")
    @JsonProperty("first_activity_rule")
    private ActivityRule firstActivityRule;

    @ApiModelProperty(value = "当前满足的活动梯度，如果为空则表示没有满足的梯度")
    @JsonProperty("cur_activity_rule")
    private ActivityRule curActivityRule;

}
