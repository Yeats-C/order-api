package com.aiqin.mgs.order.api.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
@ApiModel("TOB订单月汇总")
public class OrderMonthCalculateInfo{
	
	
	@ApiModelProperty(value="门店ID")
	@JsonProperty("store_id")
	private String storeId;

	@ApiModelProperty(value="门店编码")
	@JsonProperty("store_code")
	private String storeCode;

	@ApiModelProperty(value="门店名称")
	@JsonProperty("store_name")
	private String storeName;

	@ApiModelProperty(value="总销售实际完成")
	@JsonProperty("total_finish")
	private Integer totalFinish;
	
	@ApiModelProperty(value="18SA销售实际完成")
	@JsonProperty("eighteen_finish")
	private Integer eighteenFinish;

	@ApiModelProperty(value="服纺销售实际值")
	@JsonProperty("textile_finish")
	private Integer textileFinish;
	
	@ApiModelProperty(value="汇总时间  YYYY-MM")
	@JsonProperty("calculate_month")
	private String calculateMonth;
	
}