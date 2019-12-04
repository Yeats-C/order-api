package com.aiqin.mgs.order.api.domain.pay;

import com.aiqin.mgs.order.api.component.AccountTypeEnum;
import com.aiqin.mgs.order.api.component.PayOriginTypeEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@ApiModel("转账参数")
public class BalancePayReq implements Serializable{

    private static final long serialVersionUID = -7002516362388729423L;

    @ApiModelProperty("订单号")
    @JsonProperty("order_no")
    private String  orderNo;

    @ApiModelProperty("业务流水号")
    @JsonProperty("business_order_no")
    private String  businessOrderNo;

    @ApiModelProperty("订单金额")
    @JsonProperty("order_amount")
    private Long orderAmount;

    @ApiModelProperty(value = "下单时间",hidden = true)
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("order_time")
    private Date orderTime;

    @ApiModelProperty("支付类型(1.微信，2支付宝，3网联，4余额转账)")
    @JsonProperty("pay_type")
    private Integer payType;

    @JsonProperty("order_source")
    @ApiModelProperty("订单来源（1.门店，2商城）")
    private Integer orderSource;

    @ApiModelProperty(value = "付款方",hidden = true)
    @JsonProperty("payer")
    private String payer;

    @ApiModelProperty(value = "收款方",hidden = true)
    @JsonProperty("reciever")
    private  String reciever;

    @ApiModelProperty("访问终端类型1：Mobile；2pc")
    @JsonProperty("source")
    private  Integer source;

    @ApiModelProperty(value = "创建人编号",hidden = true)
    @JsonProperty("create_by")
    private  String createBy;

    @ApiModelProperty(value = "支付地址",hidden = true)
    @JsonProperty("back_url")
    private  String backUrl;

    @ApiModelProperty(value = "支付来源")
    @JsonProperty("pay_origin_type")
    private Integer payOriginType= PayOriginTypeEnum.TOC_POS.getCode();

    @ApiModelProperty(value = "收款方账户类型  0:收款账户,1:平台账户,2:配送账户,3:直送账户,4:门店账户")
    @JsonProperty("reciever_account_type")
    private Integer recieverAccountType= AccountTypeEnum.ACCOUNTTYPE_2.getCode();

    @ApiModelProperty(value = "订单类型 3：转账，4退款")
    private Integer type;

    @ApiModelProperty(value = "爱亲业务主键ID")
    @JsonProperty("aiqin_merchant_id")
    private String aiqinMerchantId;

    @ApiModelProperty(value = "转账成功后跳转地址")
    @JsonProperty("jump_url")
    private String jumpUrl="http://merchant.bms.aiqin.com/#/storeManage/deliveryReplenish-order";
}
