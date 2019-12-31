package com.aiqin.mgs.order.api.domain.request.returnorder;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * description: 冲减单入参实体类详情
 * date: 2019/12/31 10:32
 * author: hantao
 * version: 1.0
 */
@ApiModel("冲减单入参实体类详情")
@Data
public class WriteDownOrderDetail implements Serializable {

//    @ApiModelProperty(value="订单详情业务id")
//    private String orderInfoDetailId;

//    @ApiModelProperty(value="分摊总价价")
//    private BigDecimal totalAmount;
//
//    @ApiModelProperty(value="分摊后单价")
//    private BigDecimal preferentialAmount;

    @ApiModelProperty(value="行号")
    private Long lineCode;

    @ApiModelProperty(value="退款数量")
    private Long returnAmount;

//    @ApiModelProperty(value="商品总数量")
//    private Long productAmount;


}
