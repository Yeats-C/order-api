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
	@JsonProperty("actualprice")
	private Integer actualPrice;
	
	
	@ApiModelProperty(value="统计日期 返回值 日：YYYY-MM-DD 月:YYYY-MM 周:0-8 0为当前周 ")
	@JsonProperty("createtime")
	private String createtime;
	
	@ApiModelProperty(value="订单笔数")
	@JsonProperty("ordercount")
	private Integer ordercount;

	public Integer getActualPrice() {
		return actualPrice;
	}

	public void setActualPrice(Integer actualPrice) {
		this.actualPrice = actualPrice;
	}

	public Integer getOrdercount() {
		return ordercount;
	}

	public void setOrdercount(Integer ordercount) {
		this.ordercount = ordercount;
	}

	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}
}



