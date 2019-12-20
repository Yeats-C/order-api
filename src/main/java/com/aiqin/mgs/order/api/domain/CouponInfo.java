package com.aiqin.mgs.order.api.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@ApiModel("优惠券备份表")
@Data
public class CouponInfo {

    @ApiModelProperty(value="ID")
    private Integer id;

    @ApiModelProperty(value="订单ID")
    private String orderId;

    @ApiModelProperty(value="加盟商ID")
    private String franchiseeId;

    @ApiModelProperty(value="优惠券编码")
    private String couponCode;

    @ApiModelProperty(value="优惠券类型0-物流券 1-服纺券 2-A品券")
    private Integer couponType;

    @ApiModelProperty(value="优惠券名称")
    private String couponName;

    @ApiModelProperty(value="面值")
    private BigDecimal nominalValue;

    @ApiModelProperty(value="有效期开始时间")
    private Date validityStartTime;

    @ApiModelProperty(value="有效期结束时间")
    private Date validityEndTime;

    @ApiModelProperty(value="状态(0:未录入 1:已录入)")
    private Integer state;

    @ApiModelProperty(value="创建时间")
    private Date createTime;

}