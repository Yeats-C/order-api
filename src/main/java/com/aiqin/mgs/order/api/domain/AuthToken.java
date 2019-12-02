package com.aiqin.mgs.order.api.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("授权认证后用户信息")
public class AuthToken {

    @ApiModelProperty("账号id")
    @JsonProperty("account_id")
    private String accountId;

    @ApiModelProperty("认证票据")
    private String ticket;

    @ApiModelProperty("工号")
    @JsonProperty("person_id")
    private String personId;

    @ApiModelProperty("工号ticket")
    @JsonProperty("ticket_person_id")
    private String ticketPersonId;

    @ApiModelProperty("用户名")
    @JsonProperty("person_name")
    private String personName;

}
