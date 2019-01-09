/*****************************************************************

* 模块名称：封装-统计商品在各个渠道的订单数
* 开发人员: 黄祉壹
* 开发时间: 2018-12-07 

* ****************************************************************************/
package com.aiqin.mgs.order.api.domain.response;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("封装-统计商品在各个渠道的订单数")
public class ProdisorResponse{
    
	@ApiModelProperty("skucode编码")
    @JsonProperty("code")
    private String code;

//    @ApiModelProperty("来源类型3:pos，4:微商城")
//    @JsonProperty("type")
//    private String type;
    
    @ApiModelProperty("销量")
    @JsonProperty("amount")
    private Integer amount;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

    
}



