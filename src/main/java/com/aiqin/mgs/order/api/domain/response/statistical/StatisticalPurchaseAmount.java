package com.aiqin.mgs.order.api.domain.response.statistical;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Createed by sunx on 2019/4/10.<br/>
 */
@Data
@ApiModel("统计进货额信息")
public class StatisticalPurchaseAmount {

    @ApiModelProperty("当月进货额")
    @JsonProperty("purchase_amount")
    private Long purchaseAmount;

    @ApiModelProperty("昨日进货额")
    @JsonProperty("yesterday_purchase_amount")
    private Long yesterdayPurchaseAmount;
}
