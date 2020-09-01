package com.aiqin.mgs.order.api.domain.response.stock;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author wanghao
 */
@Data
@ApiModel("收银员报表收款退款响应")
public class AmountDetailsResponse {

    @ApiModelProperty("订单id")
    private String orderId;

    @ApiModelProperty("订单编号")
    private String orderCode;

    @ApiModelProperty("本单金额")
    private Long thisAmount;

    @ApiModelProperty("支付类型")
    private Integer payType;

    @ApiModelProperty("支付类型值")
    private String payTypeValue;

    @ApiModelProperty("积分抵扣")
    private Long jiFenDeduction;

    @ApiModelProperty("订单列席")
    private Integer orderType;

    @ApiModelProperty("订单类型值")
    private String orderTypeValue;

    @ApiModelProperty("订单类别")
    private String orderCategoryValue;
}
