package com.aiqin.mgs.order.api.domain;

import com.aiqin.mgs.order.api.domain.logisticsRule.LogisticsRuleInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("回显物流减免规则信息")
public class echoLogisticsRule {


    @ApiModelProperty(value = "规则类型：0单品购买数量 1单品购买金额 2累计购买数量 3累计购买金额")
    @JsonProperty("rult_type")
    private Integer rultType;

    @ApiModelProperty(value = "物流减免集合")
    @JsonProperty("logistics_list")
    private List<LogisticsRuleInfo> logisticsList;
}
