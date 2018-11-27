/*****************************************************************

* 模块名称：订单售后明细-实体类
* 开发人员: 黄祉壹
* 开发时间: 2018-11-05 

* ****************************************************************************/
package com.aiqin.mgs.order.api.domain;

import java.util.Date;

import com.aiqin.mgs.order.api.base.PagesRequest;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("订单售后明细")
public class OrderAfterSaleDetailInfo extends PagesRequest {
    
	
	@ApiModelProperty(value = "数据是总条数倒序")
    @JsonProperty("rowno")
    private Integer rowno;
	
	@ApiModelProperty(value="退货明细id")
	@JsonProperty("after_sale_detail_id")
	private String afterSaleDetailId;
	
	@ApiModelProperty(value="退货id")
	@JsonProperty("after_sale_id")
	private String afterSaleId;
	
	@ApiModelProperty(value="订单编号")
	@JsonProperty("order_code")
	private String orderCode;
	
	@ApiModelProperty(value="订单明细id")
	@JsonProperty("order_detail_id")
	private String orderDetailId;
	
	@ApiModelProperty(value="商品名称")
	@JsonProperty("product_name")
	private String productName;
	
	@ApiModelProperty(value="sku码")
	@JsonProperty("sku_code")
	private String skuCode;
	
	@ApiModelProperty(value="spu码")
	@JsonProperty("spu_code")
	private String spuCode;
	
	@ApiModelProperty(value="单价")
	@JsonProperty("price")
	private Integer price;
	
	@ApiModelProperty(value="退货数量")
	@JsonProperty("return_amount")
	private Integer returnAmount;
	
	@ApiModelProperty(value="退货金额")
	@JsonProperty("return_price")
	private Integer returnPrice;
	
	@ApiModelProperty(value="退款原因类型")
	@JsonProperty("return_reason_type")
	private Integer returnReasonType ;
	
	@ApiModelProperty(value="退款原因详情")
	@JsonProperty("return_reason_content")
	private String returnReasonContent;
	
	@ApiModelProperty(value="商品类别id")
	@JsonProperty("type_id")
	private String typeId ;
	
	@ApiModelProperty(value="商品类别名称")
	@JsonProperty("type_name")
	private String typeName ;
	
	@ApiModelProperty(value="创建时间",example = "2001-01-01 01:01:01")
	@JsonProperty("create_time")
	private Date createTime ;
	
	@ApiModelProperty(value="修改时间",example = "2001-01-01 01:01:01")
	@JsonProperty("update_time")
	private Date updateTime ;
	
	@ApiModelProperty(value="操作员")
	@JsonProperty("create_by")
	private String createBy ;
	
	@ApiModelProperty(value="修改员")
	@JsonProperty("update_by")
	private String updateBy ;

	
	
	public Integer getRowno() {
		return rowno;
	}

	public void setRowno(Integer rowno) {
		this.rowno = rowno;
	}

	public String getAfterSaleDetailId() {
		return afterSaleDetailId;
	}

	public void setAfterSaleDetailId(String afterSaleDetailId) {
		this.afterSaleDetailId = afterSaleDetailId;
	}

	public String getAfterSaleId() {
		return afterSaleId;
	}

	public void setAfterSaleId(String afterSaleId) {
		this.afterSaleId = afterSaleId;
	}

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public String getOrderDetailId() {
		return orderDetailId;
	}

	public void setOrderDetailId(String orderDetailId) {
		this.orderDetailId = orderDetailId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getSkuCode() {
		return skuCode;
	}

	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}

	public String getSpuCode() {
		return spuCode;
	}

	public void setSpuCode(String spuCode) {
		this.spuCode = spuCode;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	public Integer getReturnAmount() {
		return returnAmount;
	}

	public void setReturnAmount(Integer returnAmount) {
		this.returnAmount = returnAmount;
	}

	public Integer getReturnPrice() {
		return returnPrice;
	}

	public void setReturnPrice(Integer returnPrice) {
		this.returnPrice = returnPrice;
	}

	public Integer getReturnReasonType() {
		return returnReasonType;
	}

	public void setReturnReasonType(Integer returnReasonType) {
		this.returnReasonType = returnReasonType;
	}

	public String getReturnReasonContent() {
		return returnReasonContent;
	}

	public void setReturnReasonContent(String returnReasonContent) {
		this.returnReasonContent = returnReasonContent;
	}

	public String getTypeId() {
		return typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
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



