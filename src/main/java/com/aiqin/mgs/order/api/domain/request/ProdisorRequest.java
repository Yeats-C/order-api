/*****************************************************************

* 模块名称：封装参数-商品总库菜单-统计商品在各个渠道的订单数..
* 开发人员: hzy
* 开发时间: 2019-01-26

* ****************************************************************************/
package com.aiqin.mgs.order.api.domain.request;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.web.bind.annotation.RequestBody;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;


@Api("封装参数-商品总库菜单-统计商品在各个渠道的订单数..")
public class ProdisorRequest {

	@ApiModelProperty("skuidlist")
    @JsonProperty("sku_list")
	@NotNull
    private List<String> skuList;
	
	@ApiModelProperty("来源类型list")
    @JsonProperty("origin_type_list")
    private List<Integer> originTypeList;

	public List<String> getSkuList() {
		return skuList;
	}

	public void setSkuList(List<String> skuList) {
		this.skuList = skuList;
	}

	public List<Integer> getOriginTypeList() {
		return originTypeList;
	}

	public void setOriginTypeList(List<Integer> originTypeList) {
		this.originTypeList = originTypeList;
	}
	
	
}
