package com.aiqin.mgs.order.api.domain.request;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.List;

import com.aiqin.mgs.order.api.base.PagesRequest;
import com.fasterxml.jackson.annotation.JsonProperty;

@Api("参数-可退货的订单查询")
public class ReorerRequest extends PagesRequest{
	
	
	@ApiModelProperty(value="不传参不返回 0:全部")
	@JsonProperty("icount")
	private Integer icount = 0;
	

	public Integer getIcount() {
		return icount;
	}


	public void setIcount(Integer icount) {
		this.icount = icount;
	}
	
	@ApiModelProperty(value = "订单id")
    @JsonProperty("order_id")
    private String orderId;
	
	@ApiModelProperty(value = "订单code")
    @JsonProperty("order_code")
    private String orderCode;
	
	@ApiModelProperty(value = "查询订单状态")
    @JsonProperty("status_list")
    private List<Integer> statusList;
	
	@ApiModelProperty(value="分销机构id")
	@JsonProperty("distributor_id")
	private String distributorId="";
	
	@ApiModelProperty(value="开始时间String类型、格式:yyyy-MM-dd")
	@JsonProperty("begin_time")
	private String beginTime;


	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public List<Integer> getStatusList() {
		return statusList;
	}

	public void setStatusList(List<Integer> statusList) {
		this.statusList = statusList;
	}

	public String getDistributorId() {
		return distributorId;
	}

	public void setDistributorId(String distributorId) {
		this.distributorId = distributorId;
	}
}
