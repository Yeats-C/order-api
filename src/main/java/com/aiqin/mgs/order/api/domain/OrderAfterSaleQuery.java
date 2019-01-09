/*****************************************************************

* 模块名称：订单售后-查询条件
* 开发人员: 黄祉壹
* 开发时间: 2018-11-05 

* ****************************************************************************/
package com.aiqin.mgs.order.api.domain;
import java.util.Date;

import com.aiqin.mgs.order.api.base.PagesRequest;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("订单售后查询条件")
public class OrderAfterSaleQuery extends PagesRequest {
    
	@ApiModelProperty(value="不传参不返回 0:全部")
	@JsonProperty("icount")
	private Integer icount = 0;
	

	public Integer getIcount() {
		return icount;
	}


	public void setIcount(Integer icount) {
		this.icount = icount;
	}
	
	@ApiModelProperty(value="售后编号")
	@JsonProperty("after_sale_code")
	private String afterSaleCode ;
	
	@ApiModelProperty(value="会员手机号")
	@JsonProperty("member_phone")
	private String memberPhone ;
	
	@ApiModelProperty(value="订单id")
	@JsonProperty("order_id")
	private String orderId ;
	
	@ApiModelProperty(value="订单编号")
	@JsonProperty("order_code")
	private String orderCode ;
	
	@ApiModelProperty(value="分销机构id")
	@JsonProperty("distributor_id")
	private String distributorId;
	
	@ApiModelProperty(value="分销机构name")
	@JsonProperty("distributor_name")
	private String distributorName;
	
	@ApiModelProperty(value="售后id")
	@JsonProperty("after_sale_id")
	private String afterSaleId ;
	
	@ApiModelProperty(value="订单来源类型")
	@JsonProperty("origin_type")
	private Integer originType;
	
	@ApiModelProperty(value="订单支付方式")
	@JsonProperty("pay_type")
	private Integer payType;
	
	@ApiModelProperty(value="退货状态")
	@JsonProperty("after_sale_status")
	private Integer afterSaleStatus;
	
	
	@ApiModelProperty(value="开始时间Date类型",example = "2001-01-01 01:01:01")
	@JsonProperty("begin_time")
	private Date beginTime;
	
	@ApiModelProperty(value="结束时间Date类型",example = "2001-01-01 01:01:01")
	@JsonProperty("end_time")
	private Date endTime;
	
	@ApiModelProperty(value="申请时间",example = "2001-01-01 01:01:01")
	@JsonProperty("create_time")
	private Date createTime;
	
	
	@ApiModelProperty(value="开始时间类型:String,格式:yyyy-mm-dd")
	@JsonProperty("begin_date")
	private String beginDate ;
	
	@ApiModelProperty(value="结束时间类型:String,格式:yyyy-mm-dd")
	@JsonProperty("end_date")
	private String endDate ;
	
	@ApiModelProperty(value="订单号/手机号")
	@JsonProperty("code_and_phone")
	private String codeAndPhone;
	
	
	public String getCodeAndPhone() {
		return codeAndPhone;
	}


	public void setCodeAndPhone(String codeAndPhone) {
		this.codeAndPhone = codeAndPhone;
	}


	public String getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public Integer getPayType() {
		return payType;
	}

	public void setPayType(Integer payType) {
		this.payType = payType;
	}

	public String getMemberPhone() {
		return memberPhone;
	}

	public void setMemberPhone(String memberPhone) {
		this.memberPhone = memberPhone;
	}

	public Integer getAfterSaleStatus() {
		return afterSaleStatus;
	}

	public void setAfterSaleStatus(Integer afterSaleStatus) {
		this.afterSaleStatus = afterSaleStatus;
	}

	public Integer getOriginType() {
		return originType;
	}

	public void setOriginType(Integer originType) {
		this.originType = originType;
	}

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public String getAfterSaleCode() {
		return afterSaleCode;
	}

	public void setAfterSaleCode(String afterSaleCode) {
		this.afterSaleCode = afterSaleCode;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getDistributorId() {
		return distributorId;
	}

	public void setDistributorId(String distributorId) {
		this.distributorId = distributorId;
	}

	public String getAfterSaleId() {
		return afterSaleId;
	}

	public void setAfterSaleId(String afterSaleId) {
		this.afterSaleId = afterSaleId;
	}

	public Date getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	
	
}



