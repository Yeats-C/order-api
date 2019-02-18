/*****************************************************************

* 模块名称：封装-订单概览统计
* 开发人员: 黄祉壹
* 开发时间: 2019-02-15 

* ****************************************************************************/
package com.aiqin.mgs.order.api.domain.response.OrderNoCodeResponse;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("封装-订单概览统计")
public class SelectSumByStoreIdResonse{
    
	    
	@ApiModelProperty("总销售金额")
    @JsonProperty("price")
    private Integer price;
	
	@ApiModelProperty("总销量")
	@JsonProperty("count")
	private Integer count;

	@ApiModelProperty("当月客流量")
	@JsonProperty("passenger_flow")
	private Integer passengerFlow;
    
	@ApiModelProperty("昨日订单金额")
	@JsonProperty("yesterday_price")
	private Integer yesterdayPrice;
	
	@ApiModelProperty("昨日订单销量")
    @JsonProperty("yesterday_count")
    private Integer yesterdayCount;

	@ApiModelProperty("昨日客流量")
	@JsonProperty("yesterday_passenger_flow")
	private Integer yesterdayPassengerFlow;

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public Integer getPassengerFlow() {
		return passengerFlow;
	}

	public void setPassengerFlow(Integer passengerFlow) {
		this.passengerFlow = passengerFlow;
	}

	public Integer getYesterdayPrice() {
		return yesterdayPrice;
	}

	public void setYesterdayPrice(Integer yesterdayPrice) {
		this.yesterdayPrice = yesterdayPrice;
	}

	public Integer getYesterdayCount() {
		return yesterdayCount;
	}

	public void setYesterdayCount(Integer yesterdayCount) {
		this.yesterdayCount = yesterdayCount;
	}

	public Integer getYesterdayPassengerFlow() {
		return yesterdayPassengerFlow;
	}

	public void setYesterdayPassengerFlow(Integer yesterdayPassengerFlow) {
		this.yesterdayPassengerFlow = yesterdayPassengerFlow;
	}
}



