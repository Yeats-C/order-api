/*****************************************************************

* 模块名称：封装-商品类别销售概况
* 开发人员: 黄祉壹
* 开发时间: 2019-02-16 

* ****************************************************************************/
package com.aiqin.mgs.order.api.domain.response.OrderNoCodeResponse;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("封装-商品类别销售概况")
public class SelectSaleViewResonse{
    
	@ApiModelProperty("商品类别ID")
    @JsonProperty("type_id")
    private String typeId;
	
	@ApiModelProperty("商品类别名称")
    @JsonProperty("type_name")
    private String typeName;
	
	@ApiModelProperty("商品ID")
    @JsonProperty("product_id")
    private String productId;
	
	@ApiModelProperty("商品CODE")
    @JsonProperty("product_code")
    private String productCode;
	
	@ApiModelProperty("商品名称")
    @JsonProperty("product_name")
    private String productName;

	@ApiModelProperty("销售额")
	@JsonProperty("price")
	private Integer price;
	
	@ApiModelProperty("销量")
	@JsonProperty("count")
	private Integer count;

	@ApiModelProperty("客流量")
	@JsonProperty("passenger_flow")
	private Integer passengerFlow;
	
	public String getTypeId() {
		return typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
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

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public Integer getPassengerFlow() {
		return passengerFlow;
	}

	public void setPassengerFlow(Integer passengerFlow) {
		this.passengerFlow = passengerFlow;
	}	
	
}



