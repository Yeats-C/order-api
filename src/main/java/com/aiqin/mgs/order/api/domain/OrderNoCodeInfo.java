/*****************************************************************

* 模块名称：服务订单后台-实体类
* 开发人员: 黄祉壹
* 开发时间: 2019-02-19 

* ****************************************************************************/
package com.aiqin.mgs.order.api.domain;

import java.util.Date;
import java.util.List;

import com.aiqin.mgs.order.api.base.PagesRequest;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


@ApiModel("服务订单")
public class OrderNoCodeInfo extends PagesRequest {
	
	
	@ApiModelProperty(value="订单id")
	@JsonProperty("order_id")
	private String orderId;
	
	@ApiModelProperty(value="订单编号")
	@JsonProperty("order_code")
	private String orderCode;
	
	@ApiModelProperty(value="分销机构id")
	@JsonProperty("distributor_id")
	private String distributorId;
	
	
	@ApiModelProperty(value="分销机构编码")
	@JsonProperty("distributor_code")
	private String distributorCode;
	
	
	@ApiModelProperty(value="分销机构名称")
	@JsonProperty("distributor_name")
	private String distributorName;
	
	
	@ApiModelProperty(value="会员id")
	@JsonProperty("member_id")
	private String memberId="";
	
	@ApiModelProperty(value="会员名称")
	@JsonProperty("member_name")
	private String memberName="";
	
	@ApiModelProperty(value="会员手机号")
	@JsonProperty("member_phone")
	private String memberPhone;
	
	@ApiModelProperty(value="会员类型:1:会员 2:非会员")
	@JsonProperty("member_type")
	private Integer memberType;
	
	@ApiModelProperty(value="服务类别")
	@JsonProperty("type_id")
	private String typeId;
	
	@ApiModelProperty(value="服务类别名称")
	@JsonProperty("type_name")
	private String typeName;
	
	@ApiModelProperty(value="服务项目")
	@JsonProperty("product_id")
	private String productId;
	
	@ApiModelProperty(value="服务项目编码")
	@JsonProperty("product_code")
	private String productCode;
	
	@ApiModelProperty(value="服务项目名称")
	@JsonProperty("product_name")
	private String productName;
	
//	@ApiModelProperty(value="来源类型:2||null-全部;0&&3-门店;1-微商城;0-pos;3:web")
//	@JsonProperty("origin_type")
//	private Integer originType;
//	
//	
//	@ApiModelProperty(value="订单状态")
//	@JsonProperty("order_status")
//	private Integer orderStatus;
//	
//	
//	@ApiModelProperty(value="退货状态")
//	@JsonProperty("return_status")
//	private Integer returnStatus;
//	
//	
//	@ApiModelProperty(value="支付状态")
//	@JsonProperty("pay_status")
//	private Integer payStatus;
//	
//	
//	@ApiModelProperty(value="支付方式")
//	@JsonProperty("pay_type")
//	private String payType;
	
	
	@ApiModelProperty(value="实际金额")
	@JsonProperty("actual_price")
	private Integer actualPrice;
	
	
//	@ApiModelProperty(value="应付金额")
//	@JsonProperty("total_price")
//	private Integer totalPrice;
//	
//	
//	@ApiModelProperty(value="客户备注")
//	@JsonProperty("custom_note")
//	private String customNote;
//	
//	
//	@ApiModelProperty(value="商家备注")
//	@JsonProperty("business_note")
//	private String businessNote;
	
	@ApiModelProperty(value="收银员id")
	@JsonProperty("cashier_id")
	private String cashierId;
	
	
	@ApiModelProperty(value="收银员名称")
	@JsonProperty("cashier_name")
	private String cashierName;
	
	
	@ApiModelProperty(value="导购id")
	@JsonProperty("guide_id")
	private String guideId;
	
	
	@ApiModelProperty(value="导购名称")
	@JsonProperty("guide_name")
	private String guideName;
	
//	//20190214
//	@ApiModelProperty(value="订单类型 1：TOC订单 2: TOB订单 3：服务商品")
//	@JsonProperty("order_type")
//	private Integer orderType;
	
	@ApiModelProperty(value="下单时间",example = "2001-01-01 01:01:01")
	@JsonProperty("create_time")
	private Date createTime;

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

	public Integer getMemberType() {
		return memberType;
	}

	public void setMemberType(Integer memberType) {
		this.memberType = memberType;
	}

	public String getTypeId() {
		return typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Integer getActualPrice() {
		return actualPrice;
	}

	public void setActualPrice(Integer actualPrice) {
		this.actualPrice = actualPrice;
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

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

//	@ApiModelProperty(value="更改时间",example = "2001-01-01 01:01:01")
//	@JsonProperty("update_time")
//	private Date updateTime;
//	
//	
//	@ApiModelProperty(value="操作员")
//	@JsonProperty("create_by")
//	private String createBy;
	
	
	
	

}



