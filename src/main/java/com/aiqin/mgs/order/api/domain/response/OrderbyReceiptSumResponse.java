/*****************************************************************

* 模块名称：封装-收银员交班收银情况统计
* 开发人员: 黄祉壹
* 开发时间: 2018-12-03 

* ****************************************************************************/
package com.aiqin.mgs.order.api.domain.response;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("封装-收银员交班收银情况统计")
public class OrderbyReceiptSumResponse{
    
	@ApiModelProperty("收银员id")
    @JsonProperty("cashier_id")
    private String cashierId;
	
    @ApiModelProperty("收银员名称")
    @JsonProperty("cashier_name")
    private String cashierName;
    
    @ApiModelProperty("支付类型")
    @JsonProperty("pay_type")
    private Integer payType;
    
    @ApiModelProperty("支付金额")
    @JsonProperty("pay_price")
    private Long payPrice;
    
    @ApiModelProperty("现金金额")
    @JsonProperty("cash")
    private Long cash;
    
    @ApiModelProperty("微信付款金额")
    @JsonProperty("we_chat")
    private Long weChat;
    
    @ApiModelProperty("支付宝支付")
    @JsonProperty("ali_pay")
    private Long aliPay;
    
    @ApiModelProperty("银行卡支付")
    @JsonProperty("bank_card")
    private Long bankCard;
    
    @ApiModelProperty("退款金额")
    @JsonProperty("return_price")
    private Long returnPrice;
    
    @ApiModelProperty("销售额")
    @JsonProperty("sales_amount")
    private Long salesAmount;

	@ApiModelProperty("储值金额")
	@JsonProperty("recharge_amount")
    private Long rechargeAmount;
	@ApiModelProperty("充值笔数")
	@JsonProperty("recharge_order_amount")
	private Long rechargeOrderAmount;
    
    @ApiModelProperty("销售订单数")
    @JsonProperty("sales_order_amount")
    private Integer salesOrderAmount;
    
    @ApiModelProperty("退款订单数")
    @JsonProperty("return_order_amount")
    private Integer returnOrderAmount;

	@ApiModelProperty("上次交接时间")
	@JsonProperty("loading_start")
	private String loadingStart;


    @ApiModelProperty("微信退款金额")
    @JsonProperty("return_we_chat")
    private Long returnWeChat;

    @ApiModelProperty("支付宝退款金额")
    @JsonProperty("return_ali_pay")
    private Long returnAliPay;


}



