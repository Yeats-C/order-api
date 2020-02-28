/*****************************************************************

* 模块名称：封装返回-sku统计.
* 开发人员: hzy
* 开发时间: 2019-01-04 

* ****************************************************************************/
package com.aiqin.mgs.order.api.domain.response;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("封装返回-sku销量统计.")
public class SkuSumResponse{
    
	
	@ApiModelProperty("sku")
    @JsonProperty("sku_code")
    private String skuCode;
	
    @ApiModelProperty("日期")
    @JsonProperty("trans_date")
    private String transDate;
    
    @ApiModelProperty("数量")
    @JsonProperty("amount")
    private Integer amount;
    
    @ApiModelProperty("金额")
    @JsonProperty("price")
    private Integer price;

	public String getSkuCode() {
		return skuCode;
	}

	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}

	public String getTransDate() {
		return transDate;
	}

	public void setTransDate(String transDate) {
		this.transDate = transDate;
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
	@Override
	public String toString() {

		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);

	}
    
    
}



