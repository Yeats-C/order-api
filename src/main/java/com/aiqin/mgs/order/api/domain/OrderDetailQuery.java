/*****************************************************************

* 模块名称：订单明细-实体类
* 开发人员: 黄祉壹
* 开发时间: 2018-11-05 

* ****************************************************************************/
package com.aiqin.mgs.order.api.domain;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotBlank;

import com.aiqin.mgs.order.api.base.PagesRequest;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("订单明细查询条件")
public class OrderDetailQuery extends PagesRequest {
    
	
	//订单ID查询明细
	@ApiModelProperty(value="订单id")
	@JsonProperty("order_id")
	private String orderId ;
	
	@ApiModelProperty(value="会员名称")
	@JsonProperty("member_name")
	private String memberName ;
	
	@ApiModelProperty(value="会员手机号")
	@JsonProperty("member_phone")
	private String memberPhone ;
	
	//接口-优惠活动查询订单明细
	@ApiModelProperty(value="营销管理创建活动id")
	@JsonProperty("activity_id")
	private String activityId ;
	
	@ApiModelProperty(value="营销管理创建活动idList")
	@JsonProperty("activity_id_list")
	private List<String> activityIdList ;	
	
	//接口-会员管理-会员消费记录
	@ApiModelProperty(value="分销机构id")
	@JsonProperty("distributor_id")
	private String distributorId="";
	
	@ApiModelProperty(value="起始时间,date类型",example = "2001-01-01 01:01:01")
	@JsonProperty("begin_date")
	private Date beginDate;
	
	@ApiModelProperty(value="结束时间,date类型",example = "2001-01-01 01:01:01")
	@JsonProperty("end_date")
	private Date endDate;
	
	@ApiModelProperty(value="会员IDlist")
	@JsonProperty("memberid_list")
	private List<String>  memberidList;
	
	@ApiModelProperty(value="订单状态")
	@JsonProperty("order_status")
	private Integer orderStatus;
	
	
	@ApiModelProperty(value="sku集合")
	@JsonProperty("suk_list")
	private List<String>  sukList;
	
	@ApiModelProperty(value="来源类型")
	@JsonProperty("origin_type")
	private Integer originType;
	
	
//	//接口--商品概览 月销量/产品销量
//	@ApiModelProperty(value="年份")
//	@JsonProperty("year")
//	private String year ;
//	
//	@ApiModelProperty(value="月份")
//	@JsonProperty("month")
//	private String month ;

	public Integer getOriginType() {
		return originType;
	}

	public void setOriginType(Integer originType) {
		this.originType = originType;
	}

	public List<String> getSukList() {
		return sukList;
	}

	public void setSukList(List<String> sukList) {
		this.sukList = sukList;
	}

	public List<String> getMemberidList() {
		return memberidList;
	}

	public String getActivityId() {
		return activityId;
	}

	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}

	public Integer getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(Integer orderStatus) {
		this.orderStatus = orderStatus;
	}

	public void setMemberidList(List<String> memberidList) {
		this.memberidList = memberidList;
	}

	public String getDistributorId() {
		return distributorId;
	}

	public void setDistributorId(String distributorId) {
		this.distributorId = distributorId;
	}

	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getMemberName() {
		return memberName;
	}

	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}

	public String getMemberPhone() {
		return memberPhone;
	}

	public void setMemberPhone(String memberPhone) {
		this.memberPhone = memberPhone;
	}

	public List<String> getActivityIdList() {
		return activityIdList;
	}

	public void setActivityIdList(List<String> activityIdList) {
		this.activityIdList = activityIdList;
	}


	
}



