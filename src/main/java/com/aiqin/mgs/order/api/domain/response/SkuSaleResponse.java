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
public class SkuSaleResponse{
    
	@ApiModelProperty("编码")
    @JsonProperty("sku_code")
    private String skuCode;
	
	@ApiModelProperty("数量")
    @JsonProperty("sale_volume")
    private Integer saleVolume;
    
	public String getSkuCode() {
		return skuCode;
	}

	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}




	public Integer getSaleVolume() {
		return saleVolume;
	}




	public void setSaleVolume(Integer saleVolume) {
		this.saleVolume = saleVolume;
	}



	@Override
	public String toString() {

		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);

	}
    
    
}



