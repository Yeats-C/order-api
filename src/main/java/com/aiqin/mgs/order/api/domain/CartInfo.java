/*****************************************************************

* 模块名称：购物车后台-实体类
* 开发人员: 黄祉壹
* 开发时间: 2018-11-05 

* ****************************************************************************/
package com.aiqin.mgs.order.api.domain;

import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import com.aiqin.mgs.order.api.base.PagesRequest;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

@ApiModel("购物车")
public class CartInfo extends PagesRequest {
    
	@ApiModelProperty(value = "自增主键")
    @JsonProperty("id")
    private Long id;
	
	@ApiModelProperty(value = "总页数")
    @JsonProperty("rowno")
    private Integer rowno;

    @ApiModelProperty(value = "会员id")
    @JsonProperty("member_id")
    @NotBlank
    private String memberId="";
    
    @ApiModelProperty(value = "会员名称")
    @JsonProperty("member_name")
    @NotBlank
    private String memberName="";
    
    @ApiModelProperty(value = "会员手机号")
    @JsonProperty("member_phone")
    @NotBlank
    private String memberPhone;
    
    @ApiModelProperty(value = "商品id")
    @JsonProperty("product_id")
    @NotBlank
    private String productId="";
    
    @ApiModelProperty(value = "商品名称")
    @JsonProperty("product_name")
    @NotBlank
    private String productName="";
    
    @ApiModelProperty(value = "sku code")
    @JsonProperty("sku_code")
    @NotBlank
    private String skuCode="";
    
    @ApiModelProperty(value = "商品数量")
    @JsonProperty("amount")
    private Integer amount;
    
    
    @ApiModelProperty(value = "logo图片")
    @JsonProperty("logo")
    @NotBlank
    private String logo="";
    
    @ApiModelProperty(value = "创建时间",example = "2001-01-01 01:01:01")
    @JsonProperty("create_time")
    private Date createTime;
    
    @ApiModelProperty(value = "更新时间",example = "2001-01-01 01:01:01")
    @JsonProperty("update_time")
    private Date updateTime;
    
    @ApiModelProperty(value = "操作员")
    @JsonProperty("create_by")
    @NotBlank
    private String createBy="";
    
    @ApiModelProperty(value = "修改员")
    @JsonProperty("update_by")
    @NotBlank
    private String updateBy="";
    
    @ApiModelProperty(value = "折扣类型")
    @JsonProperty("agio_type")
    private Integer agioType;
    
    @ApiModelProperty(value = "原价")
    @JsonProperty("price")
    private Integer price;
    
    @ApiModelProperty(value = "现价")
    @JsonProperty("retail_price")
    private Integer retailPrice;
    
    @ApiModelProperty(value = "总优惠金额")
    @JsonProperty("activity_discount")
    private Integer activityDiscount;
    
    @ApiModelProperty(value = "购买件数总和")
    @JsonProperty("acount_amount")
    private Integer acountAmount;
    
    @ApiModelProperty(value = "实付金额总和")
    @JsonProperty("acount_actualprice")
    private Integer acountActualprice;

    @ApiModelProperty(value = "应付金额总和")
    @JsonProperty("acount_totalprice")
    private Integer acountTotalprice;
    
    
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

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
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

	public Integer getRowno() {
		return rowno;
	}

	public void setRowno(Integer rowno) {
		this.rowno = rowno;
	}

	public String getMemberPhone() {
		return memberPhone;
	}

	public void setMemberPhone(String memberPhone) {
		this.memberPhone = memberPhone;
	}

	public Integer getAgioType() {
		return agioType;
	}

	public void setAgioType(Integer agioType) {
		this.agioType = agioType;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	public Integer getRetailPrice() {
		return retailPrice;
	}

	public void setRetailPrice(Integer retailPrice) {
		this.retailPrice = retailPrice;
	}

	public Integer getActivityDiscount() {
		return activityDiscount;
	}

	public void setActivityDiscount(Integer activityDiscount) {
		this.activityDiscount = activityDiscount;
	}

	public Integer getAcountAmount() {
		return acountAmount;
	}

	public void setAcountAmount(Integer acountAmount) {
		this.acountAmount = acountAmount;
	}

	public Integer getAcountActualprice() {
		return acountActualprice;
	}

	public void setAcountActualprice(Integer acountActualprice) {
		this.acountActualprice = acountActualprice;
	}

	public Integer getAcountTotalprice() {
		return acountTotalprice;
	}

	public void setAcountTotalprice(Integer acountTotalprice) {
		this.acountTotalprice = acountTotalprice;
	}


    
    
}



