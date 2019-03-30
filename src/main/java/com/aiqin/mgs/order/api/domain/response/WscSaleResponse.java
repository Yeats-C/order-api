/*****************************************************************

* 模块名称：封装-微商城订单统计
* 开发人员: hzy
* 开发时间: 2019-01-03 

* ****************************************************************************/
package com.aiqin.mgs.order.api.domain.response;
import java.util.Date;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("封装-微商城订单统计")
public class WscSaleResponse{

	//今日订单数
	@ApiModelProperty("今日订单数")
    @JsonProperty("today_count")
    private Integer todayCount;
	
	//昨日订单数
	@ApiModelProperty("昨日订单数")
	@JsonProperty("yesterday_count")
	private Integer yesterdayCount;
	
	//今日订单数
	@ApiModelProperty("今日订单金额")
	@JsonProperty("today_amount")
	private Integer todayAmount;
		
	//昨日订单数
	@ApiModelProperty("昨日订单金额")
	@JsonProperty("yesterday_amount")
	private Integer yesterdayAmount;
	
	@ApiModelProperty("今日成交客户")
	@JsonProperty("today_members")
	private Integer todayMembers;
	
	
	@ApiModelProperty("昨日成交客户")
	@JsonProperty("yesterday_members")
	private Integer yesterdayMembers;
	
	@ApiModelProperty("近七日数据订单数")
	@JsonProperty("week_count")
	private Integer weekCount;
	
	@ApiModelProperty("近七日数据订单金额")
	@JsonProperty("week_amount")
	private Integer weekAmount;



	public Integer getTodayCount() {
		return todayCount;
	}

	public void setTodayCount(Integer todayCount) {
		this.todayCount = todayCount;
	}

	public Integer getYesterdayCount() {
		return yesterdayCount;
	}

	public void setYesterdayCount(Integer yesterdayCount) {
		this.yesterdayCount = yesterdayCount;
	}

	public Integer getTodayAmount() {
		return todayAmount;
	}

	public void setTodayAmount(Integer todayAmount) {
		this.todayAmount = todayAmount;
	}

	public Integer getYesterdayAmount() {
		return yesterdayAmount;
	}

	public void setYesterdayAmount(Integer yesterdayAmount) {
		this.yesterdayAmount = yesterdayAmount;
	}

	public Integer getTodayMembers() {
		return todayMembers;
	}

	public void setTodayMembers(Integer todayMembers) {
		this.todayMembers = todayMembers;
	}

	public Integer getYesterdayMembers() {
		return yesterdayMembers;
	}

	public void setYesterdayMembers(Integer yesterdayMembers) {
		this.yesterdayMembers = yesterdayMembers;
	}

	public Integer getWeekCount() {
		return weekCount;
	}

	public void setWeekCount(Integer weekCount) {
		this.weekCount = weekCount;
	}

	public Integer getWeekAmount() {
		return weekAmount;
	}

	public void setWeekAmount(Integer weekAmount) {
		this.weekAmount = weekAmount;
	}
}



