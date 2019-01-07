/*****************************************************************

* 模块名称：订单查询条件-实体类
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


@ApiModel("订单查询条件")
public class OrderQuery extends PagesRequest {
    
	@ApiModelProperty(value = "订单id")
    @JsonProperty("order_id")
    private String orderId="";
	
	@ApiModelProperty(value = "订单code")
    @JsonProperty("order_code")
    private String orderCode="";
	
    @ApiModelProperty(value = "会员id")
    @JsonProperty("member_id")
    private String memberId="";
    
    @ApiModelProperty(value = "会员名称")
    @JsonProperty("member_name")
    private String memberName="";
    
    @ApiModelProperty(value = "会员手机号")
    @JsonProperty("member_phone")
    private String memberPhone;
    
    @ApiModelProperty(value="分销机构id")
	@JsonProperty("distributor_id")
	private String distributorId="";
	
	
	@ApiModelProperty(value="分销机构编码")
	@JsonProperty("distributor_code")
	private String distributorCode="";
	
	
	@ApiModelProperty(value="分销机构名称")
	@JsonProperty("distributor_name")
	private String distributorName="";
	
	
	@ApiModelProperty(value="来源类型0-pos，1-微商城, 2-全部 ,3:web")
	@JsonProperty("origin_type")
	private Integer originType;
	
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
	
	@ApiModelProperty(value="创建时间",example = "2001-01-01 01:01:01")
	@JsonProperty("create_time")
	private Date create_time;
	
	@ApiModelProperty(value="创建开始时间Date类型",example = "2001-01-01 01:01:01")
	@JsonProperty("begin_time")
	private Date beginTime;
	
	
	@ApiModelProperty(value="创建结束时间Date类型",example = "2001-01-01 01:01:01")
	@JsonProperty("end_time")
	private Date endTime;
	
	@ApiModelProperty(value="开始时间String类型、格式YYYY-MM-DD")
	@JsonProperty("begin_date")
	private String beginDate;
	
	
	@ApiModelProperty(value="结束时间String类型、格式YYYY-MM-DD")
	@JsonProperty("end_date")
	private String endDate;
	
	
	@ApiModelProperty(value="订单状态")
	@JsonProperty("order_status")
	private Integer orderStatus;
	
	@ApiModelProperty(value="订单支付方式")
	@JsonProperty("pay_type")
	private String payType;
	
	
	@ApiModelProperty("收货方式，0-到店自提，1-快递; 2:全部  扩展字段")
    @JsonProperty("receive_type")
    private Integer receiveType;	
	
	@ApiModelProperty(value="8位提货码")
	@JsonProperty("receive_code")
	private String receiveCode;
	
	@ApiModelProperty(value="扩展查询条件")
	@JsonProperty("any")
	private Integer any;
	
	@ApiModelProperty(value="不传参不返回 0:全部")
	@JsonProperty("icount")
	private Integer icount = 0;
	
	@ApiModelProperty(value="订单号/手机号")
	@JsonProperty("code_and_phone")
	private String codeAndPhone;
	

	public String getOrderCode() {
		return orderCode;
	}


	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}


	public Integer getIcount() {
		return icount;
	}


	public void setIcount(Integer icount) {
		this.icount = icount;
	}


	public Integer getAny() {
		return any;
	}


	public void setAny(Integer any) {
		this.any = any;
	}


	public String getReceiveCode() {
		return receiveCode;
	}


	public void setReceiveCode(String receiveCode) {
		this.receiveCode = receiveCode;
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


	public String getPayType() {
		return payType;
	}


	public void setPayType(String payType) {
		this.payType = payType;
	}


	public Integer getReceiveType() {
		return receiveType;
	}


	public void setReceiveType(Integer receiveType) {
		this.receiveType = receiveType;
	}


	public String getOrderId() {
		return orderId;
	}


	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}


	public Date getCreate_time() {
		return create_time;
	}


	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
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


	public Integer getOrderStatus() {
		return orderStatus;
	}


	public void setOrderStatus(Integer orderStatus) {
		this.orderStatus = orderStatus;
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


	public String getCodeAndPhone() {
		return codeAndPhone;
	}


	public void setCodeAndPhone(String codeAndPhone) {
		this.codeAndPhone = codeAndPhone;
	}


	

	
    
}



