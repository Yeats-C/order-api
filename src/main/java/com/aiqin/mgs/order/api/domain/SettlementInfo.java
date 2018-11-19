/*****************************************************************

* 模块名称：结算后台-实体类
* 开发人员: 黄祉壹
* 开发时间: 2018-11-05 

* ****************************************************************************/
package com.aiqin.mgs.order.api.domain;

import javax.validation.constraints.NotBlank;

import com.aiqin.mgs.order.api.base.PagesRequest;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("结算")
public class SettlementInfo extends PagesRequest {
    
	@ApiModelProperty(value = "自增主键")
    @JsonProperty("id")
    private Long id;

    @ApiModelProperty(value = "订单id")
    @JsonProperty("order_id")
    @NotBlank
    private String orderId;
    
    @ApiModelProperty(value = "商品合计")
    @JsonProperty("product_sum")
    private Integer productSum;
    
    
    @ApiModelProperty(value = "运费")
    @JsonProperty("freight")
    private Integer freight;
    
    @ApiModelProperty(value = "优惠券总优惠金额")
    @JsonProperty("total_coupons_discount")
    private Integer totalCouponsDiscount;
    
    
    @ApiModelProperty(value = "积分折扣")
    @JsonProperty("point_percentage")
    private Integer pointPercentage;
    
    
    @ApiModelProperty(value = "活动优惠")
    @JsonProperty("activity_discount")
    private Integer activityDiscount;
    
    
    @ApiModelProperty(value = "订单合计")
    @JsonProperty("order_sum")
    private Integer orderSum;
    
    
    @ApiModelProperty(value = "订单应收")
    @JsonProperty("order_receivable")
    private Integer orderReceivable;
    
    
    @ApiModelProperty(value = "订单实收")
    @JsonProperty("order_actual")
    private Integer orderActual;
    
    
    @ApiModelProperty(value = "订单找零")
    @JsonProperty("order_change")
    private Integer orderChange;
    
    
    @ApiModelProperty(value = "使用积分数")
    @JsonProperty("points")
    private Integer points;
    
    
    @ApiModelProperty(value = "积分抵扣金额")
    @JsonProperty("point_discount")
    private Integer pointDiscount;
    
    
    @ApiModelProperty(value = "订单创建时间")
    @JsonProperty("create_time")
    @NotBlank
    private String createTime;
    
    
    @ApiModelProperty(value = "订单操作员")
    @JsonProperty("create_by")
    @NotBlank
    private String createBy;
    
    @ApiModelProperty(value = "支付方式")
    @JsonProperty("pay_type")
    private Integer payType;
    
    @ApiModelProperty(value = "输入现金金额")
    @JsonProperty("input_amt")
    private Integer inputAmt;
    
    @ApiModelProperty(value = "输入积分")
    @JsonProperty("input_points")
    private Integer inputPoints;
    
    
    @ApiModelProperty(value = "是否舍零")
    @JsonProperty("is_abandon")
    private Integer isAbandon;
    
    

	public Integer getIsAbandon() {
		return isAbandon;
	}

	public void setIsAbandon(Integer isAbandon) {
		this.isAbandon = isAbandon;
	}

	public Integer getInputAmt() {
		return inputAmt;
	}

	public void setInputAmt(Integer inputAmt) {
		this.inputAmt = inputAmt;
	}

	public Integer getInputPoints() {
		return inputPoints;
	}

	public void setInputPoints(Integer inputPoints) {
		this.inputPoints = inputPoints;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public Integer getProductSum() {
		return productSum;
	}

	public void setProductSum(Integer productSum) {
		this.productSum = productSum;
	}

	public Integer getFreight() {
		return freight;
	}

	public void setFreight(Integer freight) {
		this.freight = freight;
	}

	public Integer getTotalCouponsDiscount() {
		return totalCouponsDiscount;
	}

	public void setTotalCouponsDiscount(Integer totalCouponsDiscount) {
		this.totalCouponsDiscount = totalCouponsDiscount;
	}

	public Integer getPointPercentage() {
		return pointPercentage;
	}

	public void setPointPercentage(Integer pointPercentage) {
		this.pointPercentage = pointPercentage;
	}

	public Integer getActivityDiscount() {
		return activityDiscount;
	}

	public void setActivityDiscount(Integer activityDiscount) {
		this.activityDiscount = activityDiscount;
	}

	public Integer getOrderSum() {
		return orderSum;
	}

	public void setOrderSum(Integer orderSum) {
		this.orderSum = orderSum;
	}

	public Integer getOrderReceivable() {
		return orderReceivable;
	}

	public void setOrderReceivable(Integer orderReceivable) {
		this.orderReceivable = orderReceivable;
	}

	public Integer getOrderActual() {
		return orderActual;
	}

	public void setOrderActual(Integer orderActual) {
		this.orderActual = orderActual;
	}

	public Integer getOrderChange() {
		return orderChange;
	}

	public void setOrderChange(Integer orderChange) {
		this.orderChange = orderChange;
	}

	public Integer getPoints() {
		return points;
	}

	public void setPoints(Integer points) {
		this.points = points;
	}

	public Integer getPointDiscount() {
		return pointDiscount;
	}

	public void setPointDiscount(Integer pointDiscount) {
		this.pointDiscount = pointDiscount;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public Integer getPayType() {
		return payType;
	}

	public void setPayType(Integer payType) {
		this.payType = payType;
	}    
    
}



