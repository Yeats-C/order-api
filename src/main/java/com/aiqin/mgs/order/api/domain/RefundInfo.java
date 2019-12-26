package com.aiqin.mgs.order.api.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@ApiModel("支付流水表")
public class RefundInfo {

    @ApiModelProperty(value="ID")
    private Integer id;

    @ApiModelProperty(value="订单编码")
    private String orderCode;

    @ApiModelProperty(value="支付流水")
    private String payNum;

    @ApiModelProperty(value="金额")
    private BigDecimal orderAmount;

    @ApiModelProperty(value="支付类型 1:付款  2:退款")
    private Integer payType;

    @ApiModelProperty(value="状态 0:未完成 1:已完成")
    private Integer status;

    @ApiModelProperty(value="创建时间")
    private Date createTime;

    @ApiModelProperty(value="修改时间")
    private Date updateTime;

}