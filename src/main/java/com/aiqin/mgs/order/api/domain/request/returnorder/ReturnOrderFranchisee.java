package com.aiqin.mgs.order.api.domain.request.returnorder;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "订单中加盟商信息")
public class ReturnOrderFranchisee {

    @ApiModelProperty("加盟商编码")
    private String franchiseeCode;

    @ApiModelProperty("加盟商名称")
    private String franchiseeName;

    @ApiModelProperty("合伙人编码")
    private String copartnerAreaId;

    @ApiModelProperty("合伙人名称")
    private String copartnerAreaName;
}
