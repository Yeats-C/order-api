/*****************************************************************

* 模块名称：接口-订单概览-分销机构、小于当前日期内的实付金额、订单数量-实体类
* 开发人员: 黄祉壹
* 开发时间: 2018-11-05 

* ****************************************************************************/
package com.aiqin.mgs.order.api.domain.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


@ApiModel("订单")
public class OrderResponse {
    
	@ApiModelProperty(value="实际金额")
	@JsonProperty("actual_price")
	private Integer actualPrice;
	
	
	@ApiModelProperty(value="统计日期 返回值 日：YYYY-MM-DD 月:YYYY-MM 周:0-8 0为当前周 ")
	@JsonProperty("create_time")
	private String createTime;
	
	@ApiModelProperty(value="订单笔数")
	@JsonProperty("order_count")
	private Integer orderCount;

	public Integer getActualPrice() {
		return actualPrice;
	}

	public void setActualPrice(Integer actualPrice) {
		this.actualPrice = actualPrice;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public Integer getOrderCount() {
		return orderCount;
	}

	public void setOrderCount(Integer orderCount) {
		this.orderCount = orderCount;
	}

}



