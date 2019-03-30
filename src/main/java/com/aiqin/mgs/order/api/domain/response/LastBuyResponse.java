/*****************************************************************

* 模块名称：封装-收银员交班收银情况统计
* 开发人员: 黄祉壹
* 开发时间: 2018-12-03 

* ****************************************************************************/
package com.aiqin.mgs.order.api.domain.response;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("封装-收银员交班收银情况统计")
public class LastBuyResponse{
    
	@ApiModelProperty("最近消费时间")
    @JsonProperty("new_consume_time")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date newConsumeTime;

    @ApiModelProperty("最近购买商品")
    @JsonProperty("new_consume_product")
    private List<String> newConsumeProduct;
    
    @ApiModelProperty("商品-不传值不返回")
    @JsonProperty("product")
    private String product;
    
    

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public Date getNewConsumeTime() {
		return newConsumeTime;
	}

	public void setNewConsumeTime(Date newConsumeTime) {
		this.newConsumeTime = newConsumeTime;
	}

	public List<String> getNewConsumeProduct() {
		return newConsumeProduct;
	}

	public void setNewConsumeProduct(List<String> newConsumeProduct) {
		this.newConsumeProduct = newConsumeProduct;
	}
	
}



