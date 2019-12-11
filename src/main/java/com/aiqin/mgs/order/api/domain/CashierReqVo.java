package com.aiqin.mgs.order.api.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Createed by ch on 2019/12/11.<br/>
 */
@Data
@ApiModel("收银员交班结束时间")
public class CashierReqVo {

    @ApiModelProperty("工号")
    @JsonProperty("personId")
    private String personId;

    @ApiModelProperty("分销机构id，即门店id")
    @JsonProperty("distributorId")
    private String distributorId;

    @ApiModelProperty("交接时间")
    @JsonProperty("end_time")
    private String endTime;
}
