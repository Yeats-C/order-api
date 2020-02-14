package com.aiqin.mgs.order.api.domain.request.activity;

import com.aiqin.mgs.order.api.domain.Activities;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("活动新建请求参数")
public class ActivityRequest {

    /**活动信息*/
    @ApiModelProperty(value = "活动信息")
    @JsonProperty("activity")
    private Activities activity;

}
