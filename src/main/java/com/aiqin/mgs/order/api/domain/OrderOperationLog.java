/*****************************************************************

* 模块名称：订单支付-实体类
* 开发人员: 黄祉壹
* 开发时间: 2018-11-05 

* ****************************************************************************/
package com.aiqin.mgs.order.api.domain;

import com.aiqin.mgs.order.api.base.PagesRequest;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("订单操作日志")
public class OrderOperationLog extends PagesRequest {
    
	
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
	@ApiModelProperty(value="创建时间")
	@JsonProperty("create_time")
	private String createTime ;
	@ApiModelProperty(value="修改时间")
	@JsonProperty("update_time")
	private String updateTime ;
	@ApiModelProperty(value="操作员")
	@JsonProperty("create_by")
	private String createBy ;
	@ApiModelProperty(value="修改员")
	@JsonProperty("update_by")
	private String updateBy ;
	
    
}



