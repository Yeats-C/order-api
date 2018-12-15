/*****************************************************************

* 模块名称：订单收货信息表-实体类
* 开发人员: 黄祉壹
* 开发时间: 2018-11-05 

* ****************************************************************************/
package com.aiqin.mgs.order.api.domain;

import com.aiqin.mgs.order.api.base.PagesRequest;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

@ApiModel("订单收货信息表")
public class OrderReceivingInfo extends PagesRequest {
    
	
    @ApiModelProperty(value = "订单id")
    @JsonProperty("order_id")
    private String orderId="";
    
    @ApiModelProperty(value = "收货人名称")
    @JsonProperty("receive_name")
    private String receiveName="";
    
    @ApiModelProperty(value = "收货人地址")
    @JsonProperty("receive_address")
    private String receiveAddress="";
    
    @ApiModelProperty(value = "收货人手机号")
    @JsonProperty("receive_phone")
    private String receivePhone="";
    
    @ApiModelProperty(value = "创建时间",example = "2001-01-01 01:01:01")
    @JsonProperty("create_time")
    private Date createTime;
    
    @ApiModelProperty(value = "更新时间",example = "2001-01-01 01:01:01")
    @JsonProperty("update_time")
    private Date updateTime;
    
    @ApiModelProperty(value = "操作员")
    @JsonProperty("create_by")

    private String createBy="";
    
    @ApiModelProperty(value = "修改员")
    @JsonProperty("update_by")
    private String updateBy="";

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getReceiveName() {
		return receiveName;
	}

	public void setReceiveName(String receiveName) {
		this.receiveName = receiveName;
	}

	public String getReceiveAddress() {
		return receiveAddress;
	}

	public void setReceiveAddress(String receiveAddress) {
		this.receiveAddress = receiveAddress;
	}

	public String getReceivePhone() {
		return receivePhone;
	}

	public void setReceivePhone(String receivePhone) {
		this.receivePhone = receivePhone;
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



