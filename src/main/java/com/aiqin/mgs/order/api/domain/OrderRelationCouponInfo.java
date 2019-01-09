/*****************************************************************

* 模块名称：订单优惠券关系表-实体类
* 开发人员: 黄祉壹
* 开发时间: 2018-11-05 

* ****************************************************************************/
package com.aiqin.mgs.order.api.domain;

import com.aiqin.mgs.order.api.base.PagesRequest;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("订单优惠券关系表")
public class OrderRelationCouponInfo extends PagesRequest {
    
	
	@ApiModelProperty(value="主键ID")
	@JsonProperty("ordercoupon_id")
	private String ordercouponId ;
	
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
	private Integer couponDiscount;
	
	
	@ApiModelProperty(value="优惠券状态，0-使用，1-退")
	@JsonProperty("coupon_status")
	private Integer couponStatus;
	
	
	@ApiModelProperty(value="创建时间",example = "2001-01-01 01:01:01")
	@JsonProperty("create_time")
	private String createTime;
	
	
	@ApiModelProperty(value="修改时间",example = "2001-01-01 01:01:01")
	@JsonProperty("update_time")
	private String updateTime;
	
	
	@ApiModelProperty(value="操作员")
	@JsonProperty("create_by")
	private String createBy;
	
	
	@ApiModelProperty(value="修改员")
	@JsonProperty("update_by")
	private String updateBy;


	public String getOrderId() {
		return orderId;
	}


	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}


	public String getOrderDetailId() {
		return orderDetailId;
	}


	public void setOrderDetailId(String orderDetailId) {
		this.orderDetailId = orderDetailId;
	}


	public String getCouponId() {
		return couponId;
	}


	public void setCouponId(String couponId) {
		this.couponId = couponId;
	}


	public String getCouponName() {
		return couponName;
	}


	public void setCouponName(String couponName) {
		this.couponName = couponName;
	}


	public Integer getCouponDiscount() {
		return couponDiscount;
	}


	public void setCouponDiscount(Integer couponDiscount) {
		this.couponDiscount = couponDiscount;
	}


	public Integer getCouponStatus() {
		return couponStatus;
	}


	public void setCouponStatus(Integer couponStatus) {
		this.couponStatus = couponStatus;
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


	public String getOrdercouponId() {
		return ordercouponId;
	}


	public void setOrdercouponId(String ordercouponId) {
		this.ordercouponId = ordercouponId;
	}
	
    
}



