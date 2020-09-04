package com.aiqin.mgs.order.api.domain.logisticsRule;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value = "新增物流减免信息")
public class NewLogisticsRequest {

    @ApiModelProperty(value = "新物流减免规则信息")
    @JsonProperty("newLogisticsInfo")
    private NewLogisticsInfo newLogisticsInfo;

    @ApiModelProperty(value = "参与物流减免类型列表集合")
    @JsonProperty("newReduceInfoList")
    private List<NewReduceInfo> newReduceInfoList;
}
