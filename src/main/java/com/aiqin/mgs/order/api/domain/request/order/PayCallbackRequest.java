package com.aiqin.mgs.order.api.domain.request.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class PayCallbackRequest {

    @ApiModelProperty("返回标识-0成功")
    @JsonProperty("return_code")
    private String returnCode;

    @ApiModelProperty("返回信息")
    @JsonProperty("return_msg")
    private String returnMsg;

    @ApiModelProperty("订单号")
    @JsonProperty("order_no")
    private String orderNo;

    @ApiModelProperty("订单金额")
    @JsonProperty("order_amount")
    private Long orderAmount;

    @ApiModelProperty("支付流水号")
    @JsonProperty("pay_num")
    private String payNum;

    @ApiModelProperty("第三方支付流水号(保留字段)")
    @JsonProperty("third_order_no")
    private String thirdOrderNo;
}
