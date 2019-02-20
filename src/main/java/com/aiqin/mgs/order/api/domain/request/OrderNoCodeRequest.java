/*****************************************************************

* 模块名称：封装-封装-查询服务商品
* 开发人员: 黄祉壹
* 开发时间: 2019-02-18

* ****************************************************************************/
package com.aiqin.mgs.order.api.domain.request;
import com.aiqin.mgs.order.api.base.PagesRequest;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("封装-查询服务商品")
public class OrderNoCodeRequest extends PagesRequest{
    
	
	
	@ApiModelProperty(value="订单ID")
	@JsonProperty("order_id")
	private String orderId;
	
	@ApiModelProperty(value="订单编码")
	@JsonProperty("order_code")
	private String orderCode;
	
	@ApiModelProperty(value="分销机构组合查询")
	@JsonProperty("distributor_group")
	private String distributorGroup;
	
	@ApiModelProperty(value="分销机构id")
	@JsonProperty("distributor_id")
	private String distributorId;
	
	
	@ApiModelProperty(value="分销机构编码")
	@JsonProperty("distributor_code")
	private String distributorCode;
	
	
	@ApiModelProperty(value="分销机构名称")
	@JsonProperty("distributor_name")
	private String distributorName;
	
	@ApiModelProperty(value="会员手机号")
	@JsonProperty("member_phone")
	private String memberPhone;
	
	@ApiModelProperty("开始日期yyyy-mm-dd")
    @JsonProperty("begin_date")
    private String beginDate;
	
	@ApiModelProperty("结束日期yyyy-mm-dd")
    @JsonProperty("end_date")
    private String endDate;
	
	@ApiModelProperty("销售流向:1:购买 2:退次")
    @JsonProperty("order_flow")
    private Integer orderFlow;
	
	@ApiModelProperty("服务商品类别")
    @JsonProperty("type_id")
    private String typeId;
	
	@ApiModelProperty("服务商品ID")
    @JsonProperty("product_id")
    private String productId;
	
	

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getDistributorGroup() {
		return distributorGroup;
	}

	public void setDistributorGroup(String distributorGroup) {
		this.distributorGroup = distributorGroup;
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

	public String getMemberPhone() {
		return memberPhone;
	}

	public void setMemberPhone(String memberPhone) {
		this.memberPhone = memberPhone;
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

	public Integer getOrderFlow() {
		return orderFlow;
	}

	public void setOrderFlow(Integer orderFlow) {
		this.orderFlow = orderFlow;
	}

	public String getTypeId() {
		return typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}
	
	
}



