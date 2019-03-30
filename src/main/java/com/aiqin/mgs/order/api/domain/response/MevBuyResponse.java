/*****************************************************************

* 模块名称：封装返回-通过当前门店,等级会员list、 统计订单使用的会员数、返回7天内的统计.
* 开发人员: hzy
* 开发时间: 2018-12-12 

* ****************************************************************************/
package com.aiqin.mgs.order.api.domain.response;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("封装返回-通过当前门店,等级会员list、 统计订单使用的会员数、返回7天内的统计.")
public class MevBuyResponse{
    
	
    @ApiModelProperty("当天、格式:yyyy-MM-dd")
    @JsonProperty("sys_date")
    private String sysDate;
    
    @ApiModelProperty("当天-1、格式:yyyy-MM-dd")
    @JsonProperty("one_date")
    private String oneDate;
    
    @ApiModelProperty("当天-2、格式:yyyy-MM-dd")
    @JsonProperty("two_date")
    private String twoDate;
    
    @ApiModelProperty("当天-3、格式:yyyy-MM-dd")
    @JsonProperty("three_date")
    private String threeDate;
    
    @ApiModelProperty("当天-4、格式:yyyy-MM-dd")
    @JsonProperty("four_date")
    private String fourDate;
    
    @ApiModelProperty("当天-5、格式:yyyy-MM-dd")
    @JsonProperty("five_date")
    private String fiveDate;
    
    @ApiModelProperty("当天-6、格式:yyyy-MM-dd")
    @JsonProperty("six_date")
    private String sixDate;
    
    @ApiModelProperty("当天-7、格式:yyyy-MM-dd")
    @JsonProperty("seven_date")
    private String sevenDate;
    
    
    @ApiModelProperty("当天人数")
    @JsonProperty("sys_number")
    private Integer sysNumber;
    
    @ApiModelProperty("当天-1、人数")
    @JsonProperty("one_number")
    private Integer oneNumber;
    
    @ApiModelProperty("当天-2、人数")
    @JsonProperty("two_number")
    private Integer twoNumber;
    
    @ApiModelProperty("当天-3、人数")
    @JsonProperty("three_number")
    private Integer threeNumber;
    
    @ApiModelProperty("当天-4、人数")
    @JsonProperty("four_number")
    private Integer fourNumber;
    
    @ApiModelProperty("当天-5、人数")
    @JsonProperty("five_number")
    private Integer fiveNumber;
    
    @ApiModelProperty("当天-6、人数")
    @JsonProperty("six_number")
    private Integer sixNumber;
    
    @ApiModelProperty("当天-7、人数")
    @JsonProperty("seven_number")
    private Integer sevenNumber;
    

	public String getSysDate() {
		return sysDate;
	}

	public void setSysDate(String sysDate) {
		this.sysDate = sysDate;
	}

	public String getOneDate() {
		return oneDate;
	}

	public void setOneDate(String oneDate) {
		this.oneDate = oneDate;
	}

	public String getTwoDate() {
		return twoDate;
	}

	public void setTwoDate(String twoDate) {
		this.twoDate = twoDate;
	}

	public String getThreeDate() {
		return threeDate;
	}

	public void setThreeDate(String threeDate) {
		this.threeDate = threeDate;
	}

	public String getFourDate() {
		return fourDate;
	}

	public void setFourDate(String fourDate) {
		this.fourDate = fourDate;
	}

	public String getFiveDate() {
		return fiveDate;
	}

	public void setFiveDate(String fiveDate) {
		this.fiveDate = fiveDate;
	}

	public String getSixDate() {
		return sixDate;
	}

	public void setSixDate(String sixDate) {
		this.sixDate = sixDate;
	}

	public String getSevenDate() {
		return sevenDate;
	}

	public void setSevenDate(String sevenDate) {
		this.sevenDate = sevenDate;
	}

	public Integer getSysNumber() {
		return sysNumber;
	}

	public void setSysNumber(Integer sysNumber) {
		this.sysNumber = sysNumber;
	}

	public Integer getOneNumber() {
		return oneNumber;
	}

	public void setOneNumber(Integer oneNumber) {
		this.oneNumber = oneNumber;
	}

	public Integer getTwoNumber() {
		return twoNumber;
	}

	public void setTwoNumber(Integer twoNumber) {
		this.twoNumber = twoNumber;
	}

	public Integer getThreeNumber() {
		return threeNumber;
	}

	public void setThreeNumber(Integer threeNumber) {
		this.threeNumber = threeNumber;
	}

	public Integer getFourNumber() {
		return fourNumber;
	}

	public void setFourNumber(Integer fourNumber) {
		this.fourNumber = fourNumber;
	}

	public Integer getFiveNumber() {
		return fiveNumber;
	}

	public void setFiveNumber(Integer fiveNumber) {
		this.fiveNumber = fiveNumber;
	}

	public Integer getSixNumber() {
		return sixNumber;
	}

	public void setSixNumber(Integer sixNumber) {
		this.sixNumber = sixNumber;
	}

	public Integer getSevenNumber() {
		return sevenNumber;
	}

	public void setSevenNumber(Integer sevenNumber) {
		this.sevenNumber = sevenNumber;
	}
    
    
}



