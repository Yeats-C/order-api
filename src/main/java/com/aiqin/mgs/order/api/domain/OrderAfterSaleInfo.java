/*****************************************************************

* 模块名称：订单售后后台-实体类
* 开发人员: 黄祉壹
* 开发时间: 2018-11-05 

* ****************************************************************************/
package com.aiqin.mgs.order.api.domain;

import java.util.Date;
import java.util.List;

import com.aiqin.mgs.order.api.base.PagesRequest;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("订单售后后台")
public class OrderAfterSaleInfo extends PagesRequest {
  
	
	@ApiModelProperty(value="售后id")
	@JsonProperty("after_sale_id")
	private String afterSaleId ;

	@ApiModelProperty(value="售后编号")
	@JsonProperty("after_sale_code")
	private String afterSaleCode ;
	
	
	@ApiModelProperty(value="订单id")
	@JsonProperty("order_id")
	private String orderId ;
	
	
	@ApiModelProperty(value="订单编号")
	@JsonProperty("order_code")
	private String orderCode ;
	
	@ApiModelProperty(value="会员id")
	@JsonProperty("member_id")
	private String memberId;
	
	@ApiModelProperty(value="会员名称")
	@JsonProperty("member_name")
	private String memberName;
	
	@ApiModelProperty(value="会员手机号")
	@JsonProperty("member_phone")
	private String memberPhone;
	
	@ApiModelProperty(value="分销机构id")
	@JsonProperty("distributor_id")
	private String distributorId;
	
	@ApiModelProperty(value="分销机构编码")
	@JsonProperty("distributor_code")
	private String distributorCode;
	
	@ApiModelProperty(value="分销机构名称")
	@JsonProperty("distributor_name")
	private String distributorName;

	
	@ApiModelProperty(value="退款金额")
	@JsonProperty("return_price")
	private Integer returnPrice ;
	
	
	@ApiModelProperty(value="退货状态")
	@JsonProperty("after_sale_status")
	private Integer afterSaleStatus ;
	
	
	@ApiModelProperty(value="售后备注信息")
	@JsonProperty("after_sale_content")
	private String afterSaleContent;
	
	
	@ApiModelProperty(value="售后类型")
	@JsonProperty("after_sale_type")
	private Integer afterSaleType ;
	
	
	@ApiModelProperty(value="处理方式")
	@JsonProperty("process_type")
	private Integer processType ;
	
	
	@ApiModelProperty(value="收货人姓名")
	@JsonProperty("receive_name")
	private String receiveName ;
	
	
	@ApiModelProperty(value="收货人手机号")
	@JsonProperty("receive_phone")
	private String receivePhone;
	
	@ApiModelProperty(value="订单支付方式")
	@JsonProperty("pay_type")
	private Integer payType;
	
	@ApiModelProperty(value="订单来源类型")
	@JsonProperty("origin_type")
	private Integer originType;
	
	
	@ApiModelProperty(value="申请开始时间",example = "2001-01-01 01:01:01")
	@JsonProperty("begin_time")
	private Date beginTime;
	
	@ApiModelProperty(value="申请结束时间",example = "2001-01-01 01:01:01")
	@JsonProperty("end_time")
	private Date endTime;
	
	@ApiModelProperty(value="申请时间",example = "2001-01-01 01:01:01")
	@JsonProperty("create_time")
	private Date createTime;
	
	
	@ApiModelProperty(value="修改时间",example = "2001-01-01 01:01:01")
	@JsonProperty("update_time")
	private Date updateTime;
	
	
	@ApiModelProperty(value="操作员")
	@JsonProperty("create_by")
	private String createBy;
	
	
	@ApiModelProperty(value="修改员")
	@JsonProperty("update_by")
	private String updateBy;
	
	@ApiModelProperty(value="操作员名称")
	@JsonProperty("create_by_name")
	private String createByName;
	
	
	@ApiModelProperty(value="修改员名称")
	@JsonProperty("update_by_name")
	private String updateByName;
	
	@ApiModelProperty(value="售后明细数据")
	@JsonProperty("detail_list")
	private List<OrderAfterSaleDetailInfo> detailList;




	public String getCreateByName() {
		return createByName;
	}


	public void setCreateByName(String createByName) {
		this.createByName = createByName;
	}


	public String getUpdateByName() {
		return updateByName;
	}


	public void setUpdateByName(String updateByName) {
		this.updateByName = updateByName;
	}


	public List<OrderAfterSaleDetailInfo> getDetailList() {
		return detailList;
	}


	public void setDetailList(List<OrderAfterSaleDetailInfo> detailList) {
		this.detailList = detailList;
	}


	public Integer getPayType() {
		return payType;
	}


	public void setPayType(Integer payType) {
		this.payType = payType;
	}

	public String getAfterSaleId() {
		return afterSaleId;
	}


	public void setAfterSaleId(String afterSaleId) {
		this.afterSaleId = afterSaleId;
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


	public String getAfterSaleContent() {
		return afterSaleContent;
	}


	public void setAfterSaleContent(String afterSaleContent) {
		this.afterSaleContent = afterSaleContent;
	}


	public String getReceiveName() {
		return receiveName;
	}


	public void setReceiveName(String receiveName) {
		this.receiveName = receiveName;
	}


	public String getReceivePhone() {
		return receivePhone;
	}


	public void setReceivePhone(String receivePhone) {
		this.receivePhone = receivePhone;
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


	public Date getUpdateTime() {
		return updateTime;
	}


	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	
	public Integer getReturnPrice() {
		return returnPrice;
	}


	public void setReturnPrice(Integer returnPrice) {
		this.returnPrice = returnPrice;
	}


	public Integer getAfterSaleStatus() {
		return afterSaleStatus;
	}


	public void setAfterSaleStatus(Integer afterSaleStatus) {
		this.afterSaleStatus = afterSaleStatus;
	}


	public Integer getAfterSaleType() {
		return afterSaleType;
	}


	public void setAfterSaleType(Integer afterSaleType) {
		this.afterSaleType = afterSaleType;
	}


	public Integer getProcessType() {
		return processType;
	}


	public void setProcessType(Integer processType) {
		this.processType = processType;
	}


	public Integer getOriginType() {
		return originType;
	}


	public void setOriginType(Integer originType) {
		this.originType = originType;
	}
	
    
}



