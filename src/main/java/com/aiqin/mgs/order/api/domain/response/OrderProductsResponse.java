/*****************************************************************

* 模块名称：商品集合
* 开发人员: 黄祉壹
* 开发时间: 2018-11-05 

* ****************************************************************************/
package com.aiqin.mgs.order.api.domain.response;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("商品集合")
public class OrderProductsResponse {
    

	@ApiModelProperty(value="订单id")
	@JsonProperty("order_id")
	private String orderId ;
	
	
	@ApiModelProperty(value="订单明细id")
	@JsonProperty("order_detail_id")
	private String orderDetailId ;
	
	
	@ApiModelProperty(value="商品id")
	@JsonProperty("product_id")
	private String productId ;
	
	
	@ApiModelProperty(value="商品编码")
	@JsonProperty("product_code")
	private String productCode ;
	
	
	@ApiModelProperty(value="商品名称")
	@JsonProperty("product_name")
	private String productName ;
	
	
	@ApiModelProperty(value="列表名称")
	@JsonProperty("list_name")
	private String listName;
	
	
	@ApiModelProperty(value="sku_code")
	@JsonProperty("sku_code")
	private String skuCode ;
	
	
	@ApiModelProperty(value="spu_code")
	@JsonProperty("spu_code")
	private String spuCode ;
	
	
	@ApiModelProperty(value="条形码")
	@JsonProperty("bar_code")
	private String barCode ;
	
	
	@ApiModelProperty(value="规格")
	@JsonProperty("spec")
	private String spec;
	
	
	@ApiModelProperty(value="单位")
	@JsonProperty("unit")
	private String unit;
	
	
	@ApiModelProperty(value="零售价格")
	@JsonProperty("retail_price")
	private Integer retailPrice ;
	
	
	@ApiModelProperty(value="实际价格")
	@JsonProperty("actual_price")
	private Integer actualPrice ;
	
	
	@ApiModelProperty(value="购买数量")
	@JsonProperty("amount")
	private Integer amount;
	
	
	@ApiModelProperty(value="商品状态")
	@JsonProperty("product_status")
	private Integer productStatus ;
	
	
	@ApiModelProperty(value="列表图")
	@JsonProperty("logo")
	private String logo;
	
	
	@ApiModelProperty(value="分类类型id")
	@JsonProperty("type_id")
	private String typeId;
	
	
	@ApiModelProperty(value="分类类型名称")
	@JsonProperty("type_name")
	private String typeName;
	
	
	@ApiModelProperty(value="是否为赠品，0-是，1-不是")
	@JsonProperty("gift_status")
	private Integer giftStatus;
	
	
	@ApiModelProperty(value="营销管理创建活动id")
	@JsonProperty("activity_id")
	private String activityId;
	
	
	@ApiModelProperty(value="营销管理创建活动名称")
	@JsonProperty("activity_name")
	private String activityName;
	
	
	@ApiModelProperty(value="优惠券优惠金额")
	@JsonProperty("coupon_discount")
	private Integer couponDiscount;


	public String getProductId() {
		return productId;
	}


	public void setProductId(String productId) {
		this.productId = productId;
	}


	public String getProductCode() {
		return productCode;
	}


	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}


	public String getProductName() {
		return productName;
	}


	public void setProductName(String productName) {
		this.productName = productName;
	}


	public String getListName() {
		return listName;
	}


	public void setListName(String listName) {
		this.listName = listName;
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


	public String getBarCode() {
		return barCode;
	}


	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}


	public String getSpec() {
		return spec;
	}


	public void setSpec(String spec) {
		this.spec = spec;
	}


	public String getUnit() {
		return unit;
	}


	public void setUnit(String unit) {
		this.unit = unit;
	}


	public Integer getRetailPrice() {
		return retailPrice;
	}


	public void setRetailPrice(Integer retailPrice) {
		this.retailPrice = retailPrice;
	}


	public Integer getActualPrice() {
		return actualPrice;
	}


	public void setActualPrice(Integer actualPrice) {
		this.actualPrice = actualPrice;
	}


	public Integer getAmount() {
		return amount;
	}


	public void setAmount(Integer amount) {
		this.amount = amount;
	}


	public Integer getProductStatus() {
		return productStatus;
	}


	public void setProductStatus(Integer productStatus) {
		this.productStatus = productStatus;
	}


	public String getLogo() {
		return logo;
	}


	public void setLogo(String logo) {
		this.logo = logo;
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


	public Integer getGiftStatus() {
		return giftStatus;
	}


	public void setGiftStatus(Integer giftStatus) {
		this.giftStatus = giftStatus;
	}


	public String getActivityId() {
		return activityId;
	}


	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}


	public String getActivityName() {
		return activityName;
	}


	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}


	public Integer getCouponDiscount() {
		return couponDiscount;
	}


	public void setCouponDiscount(Integer couponDiscount) {
		this.couponDiscount = couponDiscount;
	}


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
	
	
	
}



