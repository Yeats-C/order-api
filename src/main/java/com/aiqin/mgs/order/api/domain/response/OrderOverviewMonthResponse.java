/*****************************************************************

* 模块名称：接口-概览-分销机构+当月维度-当月销售额、当月实收、当月支付订单数
* 开发人员: 黄祉壹
* 开发时间: 2018-11-05 

* ****************************************************************************/
package com.aiqin.mgs.order.api.domain.response;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("接口-概览-分销机构+当月维度-当月销售额、当月实收、当月支付订单数")
public class OrderOverviewMonthResponse {
    

	@ApiModelProperty("总销售额")
	@JsonProperty("all_income")
	private Integer allIncome;
	
	
	@ApiModelProperty("月销售额")
	@JsonProperty("month_sales_volume")
	private Integer monthSalesVolume;

	
	@ApiModelProperty("当月实收")
	@JsonProperty("month_cash")
	private Integer monthCash;

	@ApiModelProperty("当月支付订单数")
	@JsonProperty("month_payment_order_num")
	private Integer monthPaymentOrderNum;
	
	
	
	@ApiModelProperty("昨日销售额")
	@JsonProperty("yesterday_sales_volume")
	private Integer yesterdaySalesVolume;
	
	@ApiModelProperty("昨日实收")
	@JsonProperty("yesterday_cash")
	private Integer yesterdayCash;

	@ApiModelProperty("昨日支付订单数")
	@JsonProperty("yesterday_payment_order_num")
	private Integer yesterdayPaymentOrderNum;

	
	public Integer getAllIncome() {
		return allIncome;
	}

	public void setAllIncome(Integer allIncome) {
		this.allIncome = allIncome;
	}

	public Integer getMonthSalesVolume() {
		return monthSalesVolume;
	}

	public void setMonthSalesVolume(Integer monthSalesVolume) {
		this.monthSalesVolume = monthSalesVolume;
	}

	public Integer getMonthCash() {
		return monthCash;
	}

	public void setMonthCash(Integer monthCash) {
		this.monthCash = monthCash;
	}

	public Integer getMonthPaymentOrderNum() {
		return monthPaymentOrderNum;
	}

	public void setMonthPaymentOrderNum(Integer monthPaymentOrderNum) {
		this.monthPaymentOrderNum = monthPaymentOrderNum;
	}

	public Integer getYesterdaySalesVolume() {
		return yesterdaySalesVolume;
	}

	public void setYesterdaySalesVolume(Integer yesterdaySalesVolume) {
		this.yesterdaySalesVolume = yesterdaySalesVolume;
	}

	public Integer getYesterdayCash() {
		return yesterdayCash;
	}

	public void setYesterdayCash(Integer yesterdayCash) {
		this.yesterdayCash = yesterdayCash;
	}

	public Integer getYesterdayPaymentOrderNum() {
		return yesterdayPaymentOrderNum;
	}

	public void setYesterdayPaymentOrderNum(Integer yesterdayPaymentOrderNum) {
		this.yesterdayPaymentOrderNum = yesterdayPaymentOrderNum;
	}

	
	
}



