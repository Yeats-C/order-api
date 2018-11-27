/*****************************************************************

* 模块名称：订单后台-实体类
* 开发人员: 黄祉壹
* 开发时间: 2018-11-05 

* ****************************************************************************/
package com.aiqin.mgs.order.api.domain;

import java.util.Date;

import javax.validation.constraints.NotBlank;

import com.aiqin.mgs.order.api.base.PagesRequest;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


@ApiModel("订单")
public class OrderInfo extends PagesRequest {
    
	
	@ApiModelProperty(value = "数据是总条数倒序")
    @JsonProperty("rowno")
    private Integer rowno;
	
	
	@ApiModelProperty(value="订单id")
	@JsonProperty("order_id")
	@NotBlank
	private String orderId="";
	
	@ApiModelProperty(value="订单编号")
	@JsonProperty("order_code")
	@NotBlank
	private String orderCode="";
	
	@ApiModelProperty(value="会员id")
	@JsonProperty("member_id")
	@NotBlank
	private String memberId="";
	
	@ApiModelProperty(value="会员名称")
	@JsonProperty("member_name")
	@NotBlank
	private String memberName="";
	
	@ApiModelProperty(value="会员手机号")
	@JsonProperty("member_phone")
	@NotBlank
	private String memberPhone="";
	
	
	@ApiModelProperty(value="分销机构id")
	@JsonProperty("distributor_id")
	@NotBlank
	private String distributorId="";
	
	
	@ApiModelProperty(value="分销机构编码")
	@JsonProperty("distributor_code")
	@NotBlank
	private String distributorCode="";
	
	
	@ApiModelProperty(value="分销机构名称")
	@JsonProperty("distributor_name")
	@NotBlank
	private String distributorName="";
	
	
	@ApiModelProperty(value="来源类型")
	@JsonProperty("origin_type")
	private Integer originType;
	
	
	@ApiModelProperty(value="收货方式")
	@JsonProperty("receive_type")
	private Integer receiveType;
	
	
	@ApiModelProperty(value="订单状态")
	@JsonProperty("order_status")
	private Integer orderStatus;
	
	
	@ApiModelProperty(value="退货状态")
	@JsonProperty("return_status")
	private Integer returnStatus;
	
	
	@ApiModelProperty(value="支付状态")
	@JsonProperty("pay_status")
	private Integer payStatus;
	
	
	@ApiModelProperty(value="支付方式")
	@JsonProperty("pay_type")
	private Integer payType;
	
	
	@ApiModelProperty(value="实际金额")
	@JsonProperty("actual_price")
	private Integer actualPrice;
	
	
	@ApiModelProperty(value="应付金额")
	@JsonProperty("total_price")
	private Integer totalPrice;
	
	
	@ApiModelProperty(value="客户备注")
	@JsonProperty("custom_note")
	@NotBlank
	private String customNote;
	
	
	@ApiModelProperty(value="商家备注")
	@JsonProperty("business_note")
	@NotBlank
	private String businessNote;
	
	@ApiModelProperty(value="收银员id")
	@JsonProperty("cashier_id")
	@NotBlank
	private String cashierId;
	
	
	@ApiModelProperty(value="收银员名称")
	@JsonProperty("cashier_name")
	@NotBlank
	private String cashierName;
	
	
	@ApiModelProperty(value="导购id")
	@JsonProperty("guide_id")
	@NotBlank
	private String guideId;
	
	
	@ApiModelProperty(value="导购名称")
	@JsonProperty("guide_name")
	@NotBlank
	private String guideName;
	
	
	@ApiModelProperty(value="下单时间",example = "2001-01-01 01:01:01")
	@JsonProperty("create_time")
	private Date createTime;
	
	
	@ApiModelProperty(value="更改时间",example = "2001-01-01 01:01:01")
	@JsonProperty("update_time")
	private Date updateTime;
	
	
	@ApiModelProperty(value="操作员")
	@JsonProperty("create_by")
	@NotBlank
	private String createBy;
	
	
	@ApiModelProperty(value="修改员")
	@JsonProperty("update_by")
	@NotBlank
	private String updateBy;
	
	@ApiModelProperty(value="下单时间开始时间")
	@JsonProperty("begin_time")
	@NotBlank
	private String beginTime;
	
	@ApiModelProperty(value="下单时间结束时间")
	@JsonProperty("end_time")
	@NotBlank
	private String endTime;

	


	public Integer getRowno() {
		return rowno;
	}


	public void setRowno(Integer rowno) {
		this.rowno = rowno;
	}


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


	public String getMemberId() {
		return memberId;
	}


	public void setMemberId(String memberId) {
		this.memberId = memberId;
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


	public String getDistributorId() {
		return distributorId;
	}


	public void setDistributorId(String distributorId) {
		this.distributorId = distributorId;
	}


	public String getDistributorCode() {
		return distributorCode;
	}


	public void setDistributorCode(String distributorCode) {
		this.distributorCode = distributorCode;
	}


	public String getDistributorName() {
		return distributorName;
	}


	public void setDistributorName(String distributorName) {
		this.distributorName = distributorName;
	}


	public Integer getOriginType() {
		return originType;
	}


	public void setOriginType(Integer originType) {
		this.originType = originType;
	}


	public Integer getReceiveType() {
		return receiveType;
	}


	public void setReceiveType(Integer receiveType) {
		this.receiveType = receiveType;
	}


	public Integer getOrderStatus() {
		return orderStatus;
	}


	public void setOrderStatus(Integer orderStatus) {
		this.orderStatus = orderStatus;
	}


	public Integer getReturnStatus() {
		return returnStatus;
	}


	public void setReturnStatus(Integer returnStatus) {
		this.returnStatus = returnStatus;
	}


	public Integer getPayStatus() {
		return payStatus;
	}


	public void setPayStatus(Integer payStatus) {
		this.payStatus = payStatus;
	}

	public Integer getPayType() {
		return payType;
	}


	public void setPayType(Integer payType) {
		this.payType = payType;
	}


	public Integer getActualPrice() {
		return actualPrice;
	}


	public void setActualPrice(Integer actualPrice) {
		this.actualPrice = actualPrice;
	}


	public Integer getTotalPrice() {
		return totalPrice;
	}


	public void setTotalPrice(Integer totalPrice) {
		this.totalPrice = totalPrice;
	}


	public String getCustomNote() {
		return customNote;
	}


	public void setCustomNote(String customNote) {
		this.customNote = customNote;
	}


	public String getBusinessNote() {
		return businessNote;
	}


	public void setBusinessNote(String businessNote) {
		this.businessNote = businessNote;
	}


	public String getCashierId() {
		return cashierId;
	}


	public void setCashierId(String cashierId) {
		this.cashierId = cashierId;
	}


	public String getCashierName() {
		return cashierName;
	}


	public void setCashierName(String cashierName) {
		this.cashierName = cashierName;
	}


	public String getGuideId() {
		return guideId;
	}


	public void setGuideId(String guideId) {
		this.guideId = guideId;
	}


	public String getGuideName() {
		return guideName;
	}


	public void setGuideName(String guideName) {
		this.guideName = guideName;
	}

	public String getCreateBy() {
		return createBy;
	}


	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}


	public String getUpdateBy() {
		return updateBy;
	}


	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}


	public Date getCreateTime() {
		return createTime;
	}


	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}


	public Date getUpdateTime() {
		return updateTime;
	}


	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}


	public String getBeginTime() {
		return beginTime;
	}


	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}


	public String getEndTime() {
		return endTime;
	}


	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	
	
    
}



