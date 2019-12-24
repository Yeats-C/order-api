package com.aiqin.mgs.order.api.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel("优惠券审批详情表")
public class CouponApprovalDetail {

    @ApiModelProperty("ID")
    private Integer id;

    @ApiModelProperty("审批编号")
    private String formNo;

    @ApiModelProperty("优惠券种类：1-A品券 2-服纺券")
    private Integer couponType;

    @ApiModelProperty("优惠券金额总额度")
    private Double totalMoney;

    @ApiModelProperty("发放门店id")
    private String storeId;

    @ApiModelProperty("发放门店名称")
    private String storeName;

    @ApiModelProperty("有效期开始时间")
    private Date startTime;

    @ApiModelProperty("有效期结束时间")
    private Date endTime;

    @ApiModelProperty("创建人")
    private String creator;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("加盟商id")
    private String franchiseeId;

}