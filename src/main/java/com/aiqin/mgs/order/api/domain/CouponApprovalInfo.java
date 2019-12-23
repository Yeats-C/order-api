package com.aiqin.mgs.order.api.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel("优惠券审批表")
public class CouponApprovalInfo {

    @ApiModelProperty("ID")
    private Integer id;

    @ApiModelProperty("审批编号")
    private String formNo;

    @ApiModelProperty("审批名称")
    private String processTitle;

    @ApiModelProperty("审批类型")
    private String processType;

    @ApiModelProperty("审批状态")
    private Integer status;

    @ApiModelProperty("审批状态描述")
    private String statuStr;

    @ApiModelProperty("发起人姓名")
    private String applierName;

    @ApiModelProperty("发起人ID")
    private String applierId;

    @ApiModelProperty("申请时间")
    private Date createTimeStr;

    @ApiModelProperty("操作人")
    private String preNodeOptUser;

    @ApiModelProperty("操作时间")
    private Date preNodeTime;

}