package com.aiqin.mgs.order.api.domain.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.aiqin.mgs.order.api.base.PagesRequest;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Api("参数-可退货的订单查询")
@Data
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
	
	@ApiModelProperty(value="结束时间String类型、格式:yyyy-MM-dd")
	@JsonProperty("end_time")
	private String endTime;


	@JsonProperty("update_time")
	@JsonFormat( pattern = "yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty("更新时间")
	public Date updateTime;
	public String getEndTime() {
		return endTime;
	}



}
