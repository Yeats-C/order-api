/*****************************************************************

* 模块名称：订单支付-实体类
* 开发人员: 黄祉壹
* 开发时间: 2018-11-05 

* ****************************************************************************/
package com.aiqin.mgs.order.api.domain;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("订单操作日志")
public class OrderLog{
    
	@ApiModelProperty(value="日志id")
	@JsonProperty("log_id")
	private String logId;
	
	@ApiModelProperty(value="订单id")
	@JsonProperty("order_id")
	private String orderId;
	
	@ApiModelProperty(value="状态")
	@JsonProperty("status")
	private String status ;
	
	@ApiModelProperty(value="状态编码")
	@JsonProperty("status_code")
	private String statusCode ;
	
	@ApiModelProperty(value="状态描述")
	@JsonProperty("status_content")
	private String statusContent;
	
	@ApiModelProperty(value="创建时间",example = "2001-01-01 01:01:01")
	@JsonProperty("create_time")
	private String createTime ;
	
	@ApiModelProperty(value="修改时间",example = "2001-01-01 01:01:01")
	@JsonProperty("update_time")
	private String updateTime ;
	
	@ApiModelProperty(value="操作员")
	@JsonProperty("create_by")
	private String createBy ;
	
	@ApiModelProperty(value="修改员")
	@JsonProperty("update_by")
	private String updateBy ;

	
	public String getLogId() {
		return logId;
	}

	public void setLogId(String logId) {
		this.logId = logId;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public String getStatusContent() {
		return statusContent;
	}

	public void setStatusContent(String statusContent) {
		this.statusContent = statusContent;
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



