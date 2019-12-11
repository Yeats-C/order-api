package com.aiqin.mgs.order.api.domain.pay;

import com.aiqin.mgs.order.api.component.OperationTypeEnum;
import com.aiqin.mgs.order.api.component.PayOriginTypeEnum;
import com.aiqin.mgs.order.api.component.ServiceSceneEnum;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 被扫reqvo
 */
@Data
public class ToBPayReq{


    private static final long serialVersionUID = -2797140355522762575L;
    @ApiModelProperty(value = "订单金额")
    @JsonProperty("order_amount")
    private Long orderAmount;

    @ApiModelProperty(value = "支付类型(1.微信，2支付宝，3网联，4 转账)")
    @JsonProperty("pay_type")
    private Integer payType;

    @ApiModelProperty("订单来源（0:pos 1：微商城  2：全部  3：web ）")
    @JsonProperty("order_source")
    private Integer orderSource;

    @ApiModelProperty("访问终端类型1：Mobile；2pc ")
    @JsonProperty("source")
    private Integer source;

    @ApiModelProperty(value = "业务场景: 0 TO C,1 TO B")
    @JsonProperty("service_scene")
    private Integer serviceScene= ServiceSceneEnum.TO_B.getCode();

    @ApiModelProperty(value = "支付来源")
    @JsonProperty("pay_origin_type")
    private Integer payOriginType= PayOriginTypeEnum.RECHARGE.getCode();

    @ApiModelProperty("1:充值；2：消费")
    @JsonProperty("type")
    private  Integer type= OperationTypeEnum.CONSUME.getCode();
}
