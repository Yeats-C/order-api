/*****************************************************************

* 模块名称：订单售后后台-集合
* 开发人员: 黄祉壹
* 开发时间: 2018-11-05 

* ****************************************************************************/
package com.aiqin.mgs.order.api.domain;

import java.util.List;

import com.aiqin.mgs.order.api.base.PagesRequest;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("订单售后表+订单售后明细表-集合")
public class OrderAfteIListInfo extends PagesRequest {
    
	@ApiModelProperty(value = "订单售后明细列表")
    @JsonProperty("orderafterdetaillist")
    private List<OrderAfterSaleDetailInfo> orderAfterDetailList;
	
    @ApiModelProperty(value = "订单售后表")
    @JsonProperty("orderaftersaleinfo")
    private OrderAfterSaleInfo orderaftersaleinfo;

	public List<OrderAfterSaleDetailInfo> getOrderAfterDetailList() {
		return orderAfterDetailList;
	}

	public void setOrderAfterDetailList(List<OrderAfterSaleDetailInfo> orderAfterDetailList) {
		this.orderAfterDetailList = orderAfterDetailList;
	}

	public OrderAfterSaleInfo getOrderaftersaleinfo() {
		return orderaftersaleinfo;
	}

	public void setOrderaftersaleinfo(OrderAfterSaleInfo orderaftersaleinfo) {
		this.orderaftersaleinfo = orderaftersaleinfo;
	}
    
    
}



