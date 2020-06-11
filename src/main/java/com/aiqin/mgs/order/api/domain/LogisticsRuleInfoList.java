package com.aiqin.mgs.order.api.domain;

import com.aiqin.mgs.order.api.domain.LogisticsRuleType;
import com.aiqin.mgs.order.api.domain.logisticsRule.LogisticsRuleInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("新增物流免减规则")
public class LogisticsRuleInfoList {

    @ApiModelProperty(value = "规则类型信息")
    @JsonProperty("logistics_rule_type")
    private LogisticsRuleType logisticsRuleType;

    @ApiModelProperty(value = "物流减免集合")
    @JsonProperty("logistics_list")
    private List<LogisticsRuleInfo> logisticsList;


}
