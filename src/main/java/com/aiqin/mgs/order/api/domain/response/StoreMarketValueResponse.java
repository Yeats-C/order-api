package com.aiqin.mgs.order.api.domain.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel("查询门店首单赠送市值信息")
public class StoreMarketValueResponse {

    @ApiModelProperty(value="门店code")
    @JsonProperty("store_code")
    private String storeCode;

    @ApiModelProperty(value="首单赠送市值金额")
    @JsonProperty("market_value")
    private Double marketValue;

    @ApiModelProperty(value="首单赠送费用")
    @JsonProperty("free_cost")
    private Double freeCost;

    @ApiModelProperty(value="首单赠送市值余额")
    @JsonProperty("market_value_balance")
    private Double marketValueBalance;

    @ApiModelProperty(value="首单赠送费用余额")
    @JsonProperty("free_cost_balance")
    private Double freeCostBalance;

    @ApiModelProperty(value="修改时间")
    @JsonProperty("update_time")
    private Date updateTime;

    @ApiModelProperty(value="修改人id")
    @JsonProperty("update_person_id")
    private String updatePersonId;

    @ApiModelProperty(value="修改人名称")
    @JsonProperty("update_person_name")
    private String updatePersonName;
}
