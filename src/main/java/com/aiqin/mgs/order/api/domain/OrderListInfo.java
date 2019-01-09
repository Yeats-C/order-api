/*****************************************************************

* 模块名称：订单后台-集合
* 开发人员: 黄祉壹
* 开发时间: 2018-11-05 

* ****************************************************************************/
package com.aiqin.mgs.order.api.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


@ApiModel("订单后台-集合")
public class OrderListInfo  {
    

	@ApiModelProperty(value = "订单结算信息表")
    @JsonProperty("settlementlist")
    private List<SettlementInfo> settlementlist;
	
    @ApiModelProperty(value = "订单表")
    @JsonProperty("orderinfo")
    private OrderInfo orderInfo;
    
    @ApiModelProperty(value = "订单支付表")
    @JsonProperty("Orderpayinfolist")
    private List<OrderPayInfo> OrderPayInfolist;
    
//    @ApiModelProperty(value = "订单发货信息表")
//    @JsonProperty("orderpay_")
//    private List<SettlementInfo> order_pay;
    
    @ApiModelProperty(value = "订单明细表")
    @JsonProperty("orderdetailinfolist")
    private List<OrderDetailInfo> orderdetailinfo;
    
//    @ApiModelProperty(value = "订单优惠券关系表")
//    @JsonProperty("orderpay_")
//    private List<SettlementInfo> order_pay;
    
    @ApiModelProperty(value = "订单售后表 ")
    @JsonProperty("orderaftersaleinfo")
    private List<OrderAfterSaleInfo> OrderAfterSaleInfolist;

    
    @ApiModelProperty(value = "订单售后明细表")
    @JsonProperty("OrderAfterSaleDetailInfolist")
    private List<OrderAfterSaleDetailInfo> OrderAfterSaleDetailInfolist;


	public List<SettlementInfo> getSettlementlist() {
		return settlementlist;
	}


	public void setSettlementlist(List<SettlementInfo> settlementlist) {
		this.settlementlist = settlementlist;
	}


	public OrderInfo getOrderInfo() {
		return orderInfo;
	}


	public void setOrderInfo(OrderInfo orderInfo) {
		this.orderInfo = orderInfo;
	}


	public List<OrderPayInfo> getOrderPayInfolist() {
		return OrderPayInfolist;
	}


	public void setOrderPayInfolist(List<OrderPayInfo> orderPayInfolist) {
		OrderPayInfolist = orderPayInfolist;
	}


	public List<OrderDetailInfo> getOrderdetailinfo() {
		return orderdetailinfo;
	}


	public void setOrderdetailinfo(List<OrderDetailInfo> orderdetailinfo) {
		this.orderdetailinfo = orderdetailinfo;
	}


	public List<OrderAfterSaleInfo> getOrderAfterSaleInfolist() {
		return OrderAfterSaleInfolist;
	}


	public void setOrderAfterSaleInfolist(List<OrderAfterSaleInfo> orderAfterSaleInfolist) {
		OrderAfterSaleInfolist = orderAfterSaleInfolist;
	}


	public List<OrderAfterSaleDetailInfo> getOrderAfterSaleDetailInfolist() {
		return OrderAfterSaleDetailInfolist;
	}


	public void setOrderAfterSaleDetailInfolist(List<OrderAfterSaleDetailInfo> orderAfterSaleDetailInfolist) {
		OrderAfterSaleDetailInfolist = orderAfterSaleDetailInfolist;
	}

    
//    @ApiModelProperty(value = "订单收货信息表")
//    @JsonProperty("orderpay_")
//    private List<SettlementInfo> order_pay;
    
    
    
}



