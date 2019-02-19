/*****************************************************************

* 模块名称：封装-添加退货订单返回值
* 开发人员: 黄祉壹
* 开发时间: 2019-02-19

* ****************************************************************************/
package com.aiqin.mgs.order.api.domain.response.OrderNoCodeResponse;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("封装-添加退货订单返回值")
public class AddReturnOrderResonse{
    
	@ApiModelProperty("退货订单ID")
    @JsonProperty("after_sale_id")
    private String afterSaleId;
	
	@ApiModelProperty("退货订单CODE")
    @JsonProperty("after_sale_code")
    private String afterSaleCode;

	public String getAfterSaleId() {
		return afterSaleId;
	}

	public void setAfterSaleId(String afterSaleId) {
		this.afterSaleId = afterSaleId;
	}

	public String getAfterSaleCode() {
		return afterSaleCode;
	}

	public void setAfterSaleCode(String afterSaleCode) {
		this.afterSaleCode = afterSaleCode;
	}
	
	
}



