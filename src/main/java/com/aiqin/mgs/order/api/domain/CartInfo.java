/*****************************************************************

* 模块名称：购物车后台-实体类
* 开发人员: 黄祉壹
* 开发时间: 2018-11-05 

* ****************************************************************************/
package com.aiqin.mgs.order.api.domain;

import com.aiqin.mgs.order.api.base.PagesRequest;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.util.List;

@ApiModel("购物车")
public class CartInfo extends PagesRequest {

	@ApiModelProperty(value="不传参不返回 0:全部")
	@JsonProperty("icount")
	private Integer icount = 0;

	public Integer getIcount() {
		return icount;
	}

	public void setIcount(Integer icount) {
		this.icount = icount;
	}
	
	@ApiModelProperty(value = "自增主键")
    @JsonProperty("id")
    private Long id;

    @ApiModelProperty(value = "会员id")
    @JsonProperty("member_id")
    private String memberId="";
    
    @ApiModelProperty(value = "会员名称")
    @JsonProperty("member_name")
    private String memberName="";
    
    @ApiModelProperty(value = "会员手机号")
    @JsonProperty("member_phone")
    private String memberPhone;
    
    @ApiModelProperty(value = "商品id")
    @JsonProperty("product_id")
    private String productId="";
    
    @ApiModelProperty(value = "商品名称")
    @JsonProperty("product_name")
    private String productName="";

    @ApiModelProperty(value = "sku code")
    @JsonProperty("sku_code")
    private String skuCode="";
    
    @ApiModelProperty(value = "商品数量")
    @JsonProperty("amount")
    private Integer amount;
    
    
    @ApiModelProperty(value = "logo图片")
    @JsonProperty("logo")
    private String logo="";
    
    @ApiModelProperty(value = "创建时间",example = "2001-01-01 01:01:01")
	@JsonProperty("create_time")
	private Date createTime;
    
    @ApiModelProperty(value = "更新时间",example = "2001-01-01 01:01:01")
    @JsonProperty("update_time")
    private Date updateTime;
    
    @ApiModelProperty(value = "操作员")
    @JsonProperty("create_by")
    private String createBy="";
    
    @ApiModelProperty(value = "修改员")
    @JsonProperty("update_by")
    private String updateBy="";
    
    @ApiModelProperty(value = "折扣类型  1:限时折扣 2:优惠券")
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
    
    
    @ApiModelProperty(value = "分销机构id")
    @JsonProperty("distributor_id")
    private String distributorId="";
	
	@ApiModelProperty(value = "分销机构编码")
    @JsonProperty("distributor_code")
    private String distributorCode="";
	
	@ApiModelProperty(value = "分销机构名称")
    @JsonProperty("distributor_name")
    private String distributorName="";
	
	@ApiModelProperty(value = "0:添加1:修改")
    @JsonProperty("modity_type")
    private String modityType="";
	
	
	@ApiModelProperty(value = "SKUCODE查询条件")
    @JsonProperty("sku_codes")
    private List<String> skuCodes;
    
    
	
	public List<String> getSkuCodes() {
		return skuCodes;
	}

	public void setSkuCodes(List<String> skuCodes) {
		this.skuCodes = skuCodes;
	}

	public String getModityType() {
		return modityType;
	}

	public void setModityType(String modityType) {
		this.modityType = modityType;
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



