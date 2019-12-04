package com.aiqin.mgs.order.api.domain.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("支付主扫 响应vo")
public class PartnerGetCodeUrlRep implements Serializable {


    private static final long serialVersionUID = 216007573518754286L;


    @ApiModelProperty("爱亲会员主键ID")
    @JsonProperty("allinpay_member_id")
    private String allinpayMemberId;

    @ApiModelProperty("爱亲业务主键ID")
    @JsonProperty("aiqin_merchant_id")
    private String aiqinMerchantId;

    @ApiModelProperty("订单号")
    @JsonProperty("order_no")
    private  String orderNo;

    @ApiModelProperty("云通订单号")
    @JsonProperty("yun_order_no")
    private String yunOrderNo;

    @ApiModelProperty("二维码地址")
    @JsonProperty("code_url")
    private String codeUrl;

    @ApiModelProperty("处理结果代码 ，为1000支付成功")
    @JsonProperty("deal_code")
    private String dealCode;

    @ApiModelProperty("处理结果信息")
    @JsonProperty("deal_msg")
    private String dealMsg;

    @ApiModelProperty("支付类型(1.微信，2支付宝，3网联)")
    @JsonProperty("pay_type")
    private  Integer payType;


}
