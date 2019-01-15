/*****************************************************************

* 模块名称：封装-销售目标管理-分销机构-月销售额
* 开发人员: 黄祉壹
* 开发时间: 2019-01-15 

* ****************************************************************************/
package com.aiqin.mgs.order.api.domain.response;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("封装-销售目标管理-分销机构-月销售额")
public class DistributorMonthResponse{
    
    
    @ApiModelProperty("门店code")
    @JsonProperty("distributor_code")
    private String distributorCode;
    
    @ApiModelProperty("金额")
    @JsonProperty("price")
    private Integer price;

	public String getDistributorCode() {
		return distributorCode;
	}

	public void setDistributorCode(String distributorCode) {
		this.distributorCode = distributorCode;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

    
    
    
}



