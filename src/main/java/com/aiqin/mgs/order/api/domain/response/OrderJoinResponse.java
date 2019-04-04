/*****************************************************************

* 模块名称：返回订单明细数据、订单数据、收货信息、结算数据、退货数据-实体类
* 开发人员: 黄祉壹
* 开发时间: 2018-11-05 

* ****************************************************************************/
package com.aiqin.mgs.order.api.domain.response;

import java.util.List;

import com.aiqin.mgs.order.api.domain.OrderAfterSaleDetailInfo;
import com.aiqin.mgs.order.api.domain.OrderAfterSaleInfo;
import com.aiqin.mgs.order.api.domain.OrderInfo;
import com.aiqin.mgs.order.api.domain.SettlementInfo;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("查询BYorderid-返回订单数据、退货主数据、退货明细数据")
public class OrderJoinResponse {
    
	@ApiModelProperty(value = "订单主数据")
	@JsonProperty("order_info")
	private OrderInfo orderInfo;

	@ApiModelProperty(value = "结算信息")
    @JsonProperty("settlement_info")
    private SettlementInfo settlementInfo;
	
    @ApiModelProperty(value = "订单售后表")
    @JsonProperty("order_after_info")
    private OrderAfterSaleInfo orderaftersaleinfo;

	public SettlementInfo getSettlementInfo() {
		return settlementInfo;
	}

	public void setSettlementInfo(SettlementInfo settlementInfo) {
		this.settlementInfo = settlementInfo;
	}

	public OrderInfo getOrderInfo() {
		return orderInfo;
	}

	public void setOrderInfo(OrderInfo orderInfo) {
		this.orderInfo = orderInfo;
	}

	public OrderAfterSaleInfo getOrderaftersaleinfo() {
		return orderaftersaleinfo;
	}

	public void setOrderaftersaleinfo(OrderAfterSaleInfo orderaftersaleinfo) {
		this.orderaftersaleinfo = orderaftersaleinfo;
	}
    
    

}



