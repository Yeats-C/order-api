package com.aiqin.mgs.order.api.domain.request;

import com.aiqin.mgs.order.api.base.PagesRequest;
import com.aiqin.mgs.order.api.domain.ProductCycle;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Api("IdAndAmount信息")
@Data
public class OrderIdAndAmountRequest {

	@ApiModelProperty(value="ID")
	@JsonProperty("id")
	private String id;
	
	@ApiModelProperty(value="数量")
	@JsonProperty("amount")
	private Integer amount;
}
