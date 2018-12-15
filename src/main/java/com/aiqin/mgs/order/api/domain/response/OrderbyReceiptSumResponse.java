/*****************************************************************

* 模块名称：封装-收银员交班收银情况统计
* 开发人员: 黄祉壹
* 开发时间: 2018-12-03 

* ****************************************************************************/
package com.aiqin.mgs.order.api.domain.response;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

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
    
    @ApiModelProperty("销售订单数")
    @JsonProperty("sales_order_amount")
    private Integer salesOrderAmount;
    
    @ApiModelProperty("退款订单数")
    @JsonProperty("return_order_amount")
    private Integer returnOrderAmount;
    
    

	public Long getPayPrice() {
		return payPrice;
	}

	public void setPayPrice(Long payPrice) {
		this.payPrice = payPrice;
	}

	public String getCashierId() {
		return cashierId;
	}

	public void setCashierId(String cashierId) {
		this.cashierId = cashierId;
	}

	public String getCashierName() {
		return cashierName;
	}

	public void setCashierName(String cashierName) {
		this.cashierName = cashierName;
	}

	public Long getCash() {
		return cash;
	}

	public void setCash(Long cash) {
		this.cash = cash;
	}

	public Long getWeChat() {
		return weChat;
	}

	public void setWeChat(Long weChat) {
		this.weChat = weChat;
	}

	public Long getAliPay() {
		return aliPay;
	}

	public void setAliPay(Long aliPay) {
		this.aliPay = aliPay;
	}

	public Long getBankCard() {
		return bankCard;
	}

	public void setBankCard(Long bankCard) {
		this.bankCard = bankCard;
	}

	public Long getReturnPrice() {
		return returnPrice;
	}

	public void setReturnPrice(Long returnPrice) {
		this.returnPrice = returnPrice;
	}

	public Long getSalesAmount() {
		return salesAmount;
	}

	public void setSalesAmount(Long salesAmount) {
		this.salesAmount = salesAmount;
	}

	public Integer getSalesOrderAmount() {
		return salesOrderAmount;
	}

	public void setSalesOrderAmount(Integer salesOrderAmount) {
		this.salesOrderAmount = salesOrderAmount;
	}

	public Integer getReturnOrderAmount() {
		return returnOrderAmount;
	}

	public void setReturnOrderAmount(Integer returnOrderAmount) {
		this.returnOrderAmount = returnOrderAmount;
	}

	public Integer getPayType() {
		return payType;
	}

	public void setPayType(Integer payType) {
		this.payType = payType;
	}

	
	
}



