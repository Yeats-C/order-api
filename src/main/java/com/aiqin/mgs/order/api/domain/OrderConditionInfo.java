/*****************************************************************

* 模块名称：订单后台-实体类
* 开发人员: 黄祉壹
* 开发时间: 2018-11-05 

* ****************************************************************************/
package com.aiqin.mgs.order.api.domain;

import java.util.Date;

import javax.validation.constraints.NotBlank;

import com.aiqin.mgs.order.api.base.PagesRequest;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


@ApiModel("订单参数实体类")
public class OrderConditionInfo extends PagesRequest {
    
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
	
	
	@ApiModelProperty(value="来源类型")
	@JsonProperty("origin_type")
	private Integer originType;
	
	@ApiModelProperty(value="收银员id")
	@JsonProperty("cashier_id")
	@NotBlank
	private String cashierId;
	
	
	@ApiModelProperty(value="收银员名称")
	@JsonProperty("cashier_name")
	@NotBlank
	private String cashierName;
	
	
	@ApiModelProperty(value="导购id")
	@JsonProperty("guide_id")
	@NotBlank
	private String guideId;
	
	
	@ApiModelProperty(value="导购名称")
	@JsonProperty("guide_name")
	@NotBlank
	private String guideName;
	
	
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


	public Integer getOriginType() {
		return originType;
	}


	public void setOriginType(Integer originType) {
		this.originType = originType;
	}


	public String getCashierId() {
		return cashierId;
	}


	public void setCashierId(String cashierId) {
		this.cashierId = cashierId;
	}


	public String getCashierName() {
		return cashierName;
	}


	public void setCashierName(String cashierName) {
		this.cashierName = cashierName;
	}


	public String getGuideId() {
		return guideId;
	}


	public void setGuideId(String guideId) {
		this.guideId = guideId;
	}


	public String getGuideName() {
		return guideName;
	}


	public void setGuideName(String guideName) {
		this.guideName = guideName;
	}


	

	
    
}



