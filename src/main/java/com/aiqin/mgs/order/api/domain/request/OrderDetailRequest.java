package com.aiqin.mgs.order.api.domain.request;

import com.aiqin.mgs.order.api.domain.ProductCycle;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.Api;

import java.util.ArrayList;
import java.util.List;

@Api("订单明细信息")
public class OrderDetailRequest {
	@JsonProperty("orderdetail_list")
    private List<ProductCycle> orderdetail_list = new ArrayList<>();

	public List<ProductCycle> getOrderdetail_list() {
		return orderdetail_list;
	}

	public void setOrderdetail_list(List<ProductCycle> orderdetail_list) {
		this.orderdetail_list = orderdetail_list;
	}



}
