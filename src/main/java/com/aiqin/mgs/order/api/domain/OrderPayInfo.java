/*****************************************************************

* 模块名称：订单支付-实体类
* 开发人员: 黄祉壹
* 开发时间: 2018-11-05 

* ****************************************************************************/
package com.aiqin.mgs.order.api.domain;

import javax.validation.constraints.NotBlank;

import com.aiqin.mgs.order.api.base.PagesRequest;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


@ApiModel("订单支付")
public class OrderPayInfo extends PagesRequest {
    
	@ApiModelProperty(value="支付id")
	@JsonProperty("pay_id")
	private String payId="";
	
	
	@ApiModelProperty(value="订单id")
	@JsonProperty("order_id")
	@NotBlank
	private String orderId;
	
	@ApiModelProperty(value="支付类型")
	@JsonProperty("pay_type")
	private Integer payType;
	
	@ApiModelProperty(value="支付描述")
	@JsonProperty("pay_name")
	private String payName="";
	
	@ApiModelProperty(value="支付状态")
	@JsonProperty("pay_status")
	private Integer payStatus;
	
	
	@ApiModelProperty(value="支付金额")
	@JsonProperty("pay_price")
	private Integer payPrice;
	
	
	
	@ApiModelProperty(value="下单时间",example = "2001-01-01 01:01:01")
	@JsonProperty("create_time")
	private String createTime;
	
	
	@ApiModelProperty(value="更改时间",example = "2001-01-01 01:01:01")
	@JsonProperty("update_time")
	private String updateTime;
	
	
	@ApiModelProperty(value="操作员")
	@JsonProperty("create_by")
	@NotBlank
	private String createBy;
	
	
	@ApiModelProperty(value="修改员")
	@JsonProperty("update_by")
	@NotBlank
	private String updateBy;

	
	@JsonProperty("pay_way")
    @ApiModelProperty("支付方式，1.现金、2微信  3.支付宝、4银联")
    private Integer payWay;
	
	

	public Integer getPayWay() {
		return payWay;
	}


	public void setPayWay(Integer payWay) {
		this.payWay = payWay;
	}


	public String getPayId() {
		return payId;
	}


	public void setPayId(String payId) {
		this.payId = payId;
	}


	public String getOrderId() {
		return orderId;
	}


	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}


	public Integer getPayType() {
		return payType;
	}


	public void setPayType(Integer payType) {
		this.payType = payType;
	}


	public String getPayName() {
		return payName;
	}


	public void setPayName(String payName) {
		this.payName = payName;
	}


	public Integer getPayStatus() {
		return payStatus;
	}


	public void setPayStatus(Integer payStatus) {
		this.payStatus = payStatus;
	}


	public Integer getPayPrice() {
		return payPrice;
	}


	public void setPayPrice(Integer payPrice) {
		this.payPrice = payPrice;
	}


	public String getCreateTime() {
		return createTime;
	}


	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}


	public String getUpdateTime() {
		return updateTime;
	}


	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
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
	
	
    
}



