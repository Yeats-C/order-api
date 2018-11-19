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

@ApiModel("订单明细")
public class OrderRelationCoupon extends PagesRequest {
    
	
	@ApiModelProperty(value="订单id")
	@JsonProperty("order_id")
	private String orderId ;
	@ApiModelProperty(value="订单明细id")
	@JsonProperty("order_detail_id")
	private String orderDetailId ;
	@ApiModelProperty(value="优惠券id")
	@JsonProperty("coupon_id")
	private String couponId;
	@ApiModelProperty(value="优惠券名称")
	@JsonProperty("coupon_name")
	private String couponName;
	@ApiModelProperty(value="优惠券优惠金额")
	@JsonProperty("coupon_discount")
	private String couponDiscount;
	@ApiModelProperty(value="优惠券状态，0-使用，1-退")
	@JsonProperty("coupon_status")
	private String couponStatus;
	@ApiModelProperty(value="创建时间")
	@JsonProperty("create_time")
	private String createTime;
	@ApiModelProperty(value="修改时间")
	@JsonProperty("update_time")
	private String updateTime;
	@ApiModelProperty(value="操作员")
	@JsonProperty("create_by")
	private String createBy;
	@ApiModelProperty(value="修改员")
	@JsonProperty("update_by")
	private String updateBy;
	
    
}



