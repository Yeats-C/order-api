package com.aiqin.mgs.order.api.domain.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class PartnerPayGateRep {
    @ApiModelProperty("订单Id")
    @JsonProperty("order_id")
    private String  orderId;
    @ApiModelProperty("订单号")
    @JsonProperty("order_no")
    private String orderNo;
    @ApiModelProperty("云通订单号")
    @JsonProperty("yun_order_no")
    private String yunOrderNo;
    @ApiModelProperty("支付时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("pay_time")
    private Date payTime;
    @ApiModelProperty("申请支付金额")
    @JsonProperty("order_amount")
    private BigDecimal orderAmount;
    @ApiModelProperty("实际订单金额")
    @JsonProperty("actual_order_amount")
    private BigDecimal actualOrderAmount;
    @ApiModelProperty(value = "支付类型(1.微信，2支付宝，3网联，4 转账)")
    @JsonProperty("pay_type")
    private Integer payType;
    @ApiModelProperty("处理结果代码 ，为1000支付成功,1001 支付中，1005 支付失败")
    @JsonProperty("deal_code")
    private String dealCode;
    @ApiModelProperty("处理结果信息")
    @JsonProperty("deal_msg")
    private String dealMsg;
    @ApiModelProperty("订单支付状态:0订单支付处理中；1订单支付成功；2订单支付失败；3订单退款中；4订单退款成功；5订单退款失败 ；6订单未支付；7订单超时；8订单取消；9订单支付失败卡已销")
    @JsonProperty("status")
    private Integer status;

    @JsonProperty("pay_url")
    @ApiModelProperty("转账地址")
    private String  payUrl;

    @JsonProperty("rule_amount")
    @ApiModelProperty("分账金额")
    private Long  ruleAmount;

    @JsonProperty("store_id")
    @ApiModelProperty("门店id")
    private String  storeId;
}
