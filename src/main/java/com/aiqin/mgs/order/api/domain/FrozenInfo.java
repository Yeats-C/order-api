/*****************************************************************

* 模块名称：挂单-实体类
* 开发人员: 黄祉壹
* 开发时间: 2018-11-05 

* ****************************************************************************/
package com.aiqin.mgs.order.api.domain;

import javax.validation.constraints.NotBlank;

import com.aiqin.mgs.order.api.base.PagesRequest;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

@ApiModel("挂单")
public class FrozenInfo extends PagesRequest {
    
	@ApiModelProperty(value = "自增主键")
    @JsonProperty("id")
    private Long id;
	
	@ApiModelProperty(value = "挂单Id")
    @JsonProperty("frozen_id")
    @NotBlank
    private String frozenId="";
	

	@ApiModelProperty(value = "分销机构id")
    @JsonProperty("distributor_id")
    @NotBlank
    private String distributorId="";
	
	@ApiModelProperty(value = "分销机构编码")
    @JsonProperty("distributor_code")
    @NotBlank
    private String distributorCode="";
	
	@ApiModelProperty(value = "分销机构名称")
    @JsonProperty("distributor_name")
    @NotBlank
    private String distributorName="";
	
	
    @ApiModelProperty(value = "会员id")
    @JsonProperty("member_id")
    @NotBlank
    private String memberId="";
    
    @ApiModelProperty(value = "会员名称")
    @JsonProperty("member_name")
    @NotBlank
    private String memberName="";
    
    @ApiModelProperty(value = "销售员id")
    @JsonProperty("sale_byid")
    private String SaleById="";
    
    @ApiModelProperty(value = "销售员名称")
    @JsonProperty("sale_byname")
    @NotBlank
    private String SaleByName="";
    
    @ApiModelProperty(value = "商品id")
    @JsonProperty("product_id")
    @NotBlank
    private String productId="";

    
    @ApiModelProperty(value = "商品编码")
    @JsonProperty("sku_code")
    @NotBlank
    private String skuCode="";
    
//    @ApiModelProperty(value = "商品编码")
//    @JsonProperty("product_code")
//    @NotBlank
//    private String productCode="";
    
    @ApiModelProperty(value = "商品名称")
    @JsonProperty("product_name")
    @NotBlank
    private String productName="";
    

    
    @ApiModelProperty(value = "原价")
    @JsonProperty("orig_price")
    private Integer origPrice;
    
//    @ApiModelProperty(value = "现价原价百分比")
//    @JsonProperty("orig_curr_percentage")
//    private Integer origCurrPercentage;
    
    @ApiModelProperty(value = "现价")
    @JsonProperty("curr_price")
    private Integer currPrice;
    
    @ApiModelProperty(value = "数量")
    @JsonProperty("amount")
    private Integer amount;
    
    @ApiModelProperty(value = "金额")
    @JsonProperty("price")
    private Integer price;
    
    
    @ApiModelProperty(value = "挂起时间",example = "2001-01-01 01:01:01")
    @JsonProperty("create_time")
    private Date createTime;
    
    @ApiModelProperty(value = "挂起人")
    @JsonProperty("create_by")
    @NotBlank
    private String createBy="";
    
    @ApiModelProperty(value = "合计数量")
    @JsonProperty("sum_amount")
    private Integer sumAmount;
    
    @ApiModelProperty(value = "合计笔数")
    @JsonProperty("sum_sale")
    private Integer sumSale;
    
    @ApiModelProperty(value = "合计金额")
    @JsonProperty("sum_price")
    private Integer sumPrice;
    
    @ApiModelProperty(value = "备注")
    @JsonProperty("memo")
    private String memo;


	@ApiModelProperty("是否预存订单，1：是 ，2 否")
	@JsonProperty("is_prestorage")
	private Integer isPrestorage=2;

	public Integer getIsPrestorage() {
		return isPrestorage;
	}

	public void setIsPrestorage(Integer isPrestorage) {
		this.isPrestorage = isPrestorage;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
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

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public String getDistributorId() {
		return distributorId;
	}

	public void setDistributorId(String distributorId) {
		this.distributorId = distributorId;
	}

	public String getDistributorCode() {
		return distributorCode;
	}

	public void setDistributorCode(String distributorCode) {
		this.distributorCode = distributorCode;
	}

	public String getDistributorName() {
		return distributorName;
	}

	public void setDistributorName(String distributorName) {
		this.distributorName = distributorName;
	}

	public Integer getSumAmount() {
		return sumAmount;
	}

	public void setSumAmount(Integer sumAmount) {
		this.sumAmount = sumAmount;
	}

	public Integer getSumPrice() {
		return sumPrice;
	}

	public void setSumPrice(Integer sumPrice) {
		this.sumPrice = sumPrice;
	}

	public Integer getOrigPrice() {
		return origPrice;
	}

	public void setOrigPrice(Integer origPrice) {
		this.origPrice = origPrice;
	}


	public String getSaleById() {
		return SaleById;
	}

	public void setSaleById(String saleById) {
		SaleById = saleById;
	}

	public String getSaleByName() {
		return SaleByName;
	}

	public void setSaleByName(String saleByName) {
		SaleByName = saleByName;
	}

	public Integer getSumSale() {
		return sumSale;
	}

	public void setSumSale(Integer sumSale) {
		this.sumSale = sumSale;
	}

	public Integer getCurrPrice() {
		return currPrice;
	}

	public void setCurrPrice(Integer currPrice) {
		this.currPrice = currPrice;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	public String getFrozenId() {
		return frozenId;
	}

	public void setFrozenId(String frozenId) {
		this.frozenId = frozenId;
	}
	
	
    
    
}



