/*****************************************************************

* 模块名称：封装--会员管理-会员消费记录
* 开发人员: 黄祉壹
* 开发时间: 2018-12-03 

* ****************************************************************************/
package com.aiqin.mgs.order.api.domain.response;
import java.util.Date;

import javax.validation.constraints.NotBlank;

import com.aiqin.mgs.order.api.base.PagesRequest;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("封装--会员管理-会员消费记录")
public class OrderDetailByMemberResponse extends PagesRequest{
    
    @ApiModelProperty(value="会员ID")
    @JsonProperty("member_id")
    private String memberId;
		
    @ApiModelProperty(value="会员名称")
    @JsonProperty("member_name")
    private String memberName;
		
    @ApiModelProperty(value="会员手机号")
    @JsonProperty("member_phone")
    private String memberPhone;
    
    @ApiModelProperty(value="商品id")
	@JsonProperty("product_id")
	private String productId ;
	
	@ApiModelProperty(value="商品编码")
	@JsonProperty("product_code")
	private String productCode ;
	
	@ApiModelProperty(value="商品名称")
	@JsonProperty("product_name")
	private String productName ;
	
	@ApiModelProperty(value="规格")
	@JsonProperty("spec")
	private String spec ;

	@ApiModelProperty(value="来源类型")
    @JsonProperty("origin_type")
    private String originType;
	
	@ApiModelProperty(value="分销机构id")
	@JsonProperty("distributor_id")
	@NotBlank
	private String distributorId="";
	
	@ApiModelProperty(value="分销机构编码")
	@JsonProperty("distributor_code")
	@NotBlank
	private String distributorCode="";
	
	
	@ApiModelProperty(value="分销机构名称")
	@JsonProperty("distributor_name")
	@NotBlank
	private String distributorName="";
	
	@ApiModelProperty(value="提货方式")
    @JsonProperty("receive_type")
    private String receiveType;
	
	@ApiModelProperty(value="购买时间",example = "2001-01-01 01:01:01")
	@JsonProperty("create_time")
	private Date createTime;
	
	@ApiModelProperty(value="购买数量")
	@JsonProperty("amount")
	private Integer amount;
	
	@ApiModelProperty(value="消费金额")
	@JsonProperty("price")
	private Integer price;
    
    @ApiModelProperty(value="消耗周期")
    @JsonProperty("consumecycle")
    private Integer consumecycle;
		
    @ApiModelProperty(value="周期结束时间",example = "2001-01-01 01:01:01")
    @JsonProperty("cycleenddate")
    private Date cycleenddate;

    
	public String getSpec() {
		return spec;
	}

	public void setSpec(String spec) {
		this.spec = spec;
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

	public String getOriginType() {
		return originType;
	}

	public void setOriginType(String originType) {
		this.originType = originType;
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

	public String getReceiveType() {
		return receiveType;
	}

	public void setReceiveType(String receiveType) {
		this.receiveType = receiveType;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
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
    

}



