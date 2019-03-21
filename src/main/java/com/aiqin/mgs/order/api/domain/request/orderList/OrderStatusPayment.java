package com.aiqin.mgs.order.api.domain.request.orderList;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 描述:
 *
 * @author zhujunchao
 * @create 2019-03-21 17:04
 */
@ApiModel("订单支付状态修改vo")
@Data
public class OrderStatusPayment {
    @ApiModelProperty(value = "订单或父订单编号")
    @JsonProperty("order_code")
    private String orderCode;

    @ApiModelProperty(value = "支付方式描述")
    @JsonProperty("payment_type")
    private String paymentType;

    @ApiModelProperty(value = "支付方式编码")
    @JsonProperty("payment_type_code")
    private String paymentTypeCode;

    @ApiModelProperty(value = "账户余额")
    @JsonProperty("account_balance")
    private Long accountBalance;

    @ApiModelProperty(value = "授信额度")
    @JsonProperty("line_credit")
    private Long lineCredit;
}
