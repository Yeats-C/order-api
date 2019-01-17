/*****************************************************************

* 模块名称：订单明细-实体类
* 开发人员: 黄祉壹
* 开发时间: 2018-11-05 

* ****************************************************************************/
package com.aiqin.mgs.order.api.domain;

import java.util.Date;

import com.aiqin.mgs.order.api.base.PagesRequest;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("订单明细实体类")
public class OrderDetailInfo extends PagesRequest {
	
	@ApiModelProperty(value="排序")
	@JsonProperty("rowno")
	private String rowno ;
	
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
	
	
	@ApiModelProperty(value="退货状态")
	@JsonProperty("return_status")
	private Integer returnStatus;
	
	
	@ApiModelProperty(value="已退货数量")
	@JsonProperty("return_amount")
	private Integer returnAmount;
	
	
	@ApiModelProperty(value="营销管理创建活动id")
	@JsonProperty("activity_id")
	private String activityId;
	
	
	@ApiModelProperty(value="营销管理创建活动名称")
	@JsonProperty("activity_name")
	private String activityName;
	
	
	@ApiModelProperty(value="优惠券优惠金额")
	@JsonProperty("coupon_discount")
	private Integer couponDiscount;
	
	
	@ApiModelProperty(value="创建时间",example = "2001-01-01 01:01:01")
	@JsonProperty("create_time")
	private Date createTime;
	
	
	@ApiModelProperty(value="修改时间",example = "2001-01-01 01:01:01")
	@JsonProperty("update_time")
	private Date updateTime;
	
	
	@ApiModelProperty(value="操作员")
	@JsonProperty("create_by")
	private String createBy;
	
	
	@ApiModelProperty(value="修改员")
	@JsonProperty("update_by")
	private String updateBy;
	
	
	//接口-通过活动ID管理订单明细
    @ApiModelProperty(value="会员ID")
    @JsonProperty("member_id")
    private String memberId;
		
    @ApiModelProperty(value="会员名称")
    @JsonProperty("member_name")
    private String memberName;
		
    @ApiModelProperty(value="会员手机号")
    @JsonProperty("member_phone")
    private String memberPhone;
		
    @ApiModelProperty(value="来源类型:2||null-全部;0&&3-门店;1-微商城;0-pos;3:web")
    @JsonProperty("origin_type")
    private String originType;
	
	
    //接口-会员管理-会员消费记录
    @ApiModelProperty(value="消耗周期")
    @JsonProperty("consumecycle")
    private Integer consumecycle;
		
    @ApiModelProperty(value="周期结束时间")
    @JsonProperty("cycleenddate")
    private Date cycleenddate;
    
    @ApiModelProperty(value="收货方式")
    @JsonProperty("receive_type")
    private String receiveType;
    
    @ApiModelProperty(value="优惠券实体类")
    @JsonProperty("coupon_info")
    private OrderRelationCouponInfo couponInfo;
    
    
    
	public String getRowno() {
		return rowno;
	}


	public void setRowno(String rowno) {
		this.rowno = rowno;
	}


	public OrderRelationCouponInfo getCouponInfo() {
		return couponInfo;
	}


	public void setCouponInfo(OrderRelationCouponInfo couponInfo) {
		this.couponInfo = couponInfo;
	}


	public String getReceiveType() {
		return receiveType;
	}


	public void setReceiveType(String receiveType) {
		this.receiveType = receiveType;
	}


	public Integer getConsumecycle() {
		return consumecycle;
	}


	public void setConsumecycle(Integer consumecycle) {
		this.consumecycle = consumecycle;
	}


	public Date getCycleenddate() {
		return cycleenddate;
	}


	public void setCycleenddate(Date cycleenddate) {
		this.cycleenddate = cycleenddate;
	}

	public String getMemberId() {
		return memberId;
	}


	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}


	public String getMemberName() {
		return memberName;
	}


	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}


	public String getMemberPhone() {
		return memberPhone;
	}


	public void setMemberPhone(String memberPhone) {
		this.memberPhone = memberPhone;
	}


	public String getOriginType() {
		return originType;
	}


	public void setOriginType(String originType) {
		this.originType = originType;
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


	public Integer getReturnStatus() {
		return returnStatus;
	}


	public void setReturnStatus(Integer returnStatus) {
		this.returnStatus = returnStatus;
	}


	public Integer getReturnAmount() {
		return returnAmount;
	}


	public void setReturnAmount(Integer returnAmount) {
		this.returnAmount = returnAmount;
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



