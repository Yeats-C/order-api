package com.aiqin.mgs.order.api.domain.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
@ApiModel("合伙人销售报表列表")
public class ReportCopartnerSaleInfo{
	
	@ApiModelProperty(value="月份")
	@JsonProperty("report_month")
	private String reportMonth;
	
	
	@ApiModelProperty(value="经营区域ID")
	@JsonProperty("copartner_area_id")
	private String copartnerAreaId;


	@ApiModelProperty(value="经营区域名称")
	@JsonProperty("copartner_area_name")
	private String copartnerAreaName;


	@ApiModelProperty(value="门店ID")
	@JsonProperty("store_id")
	private String storeId;


	@ApiModelProperty(value="门店编码")
	@JsonProperty("store_code")
	private String storeCode;


	@ApiModelProperty(value="门店名称")
	@JsonProperty("store_name")
	private String storeName;


	@ApiModelProperty(value="总销售目标值")
	@JsonProperty("total_target")
	private Integer totalTarget;


	@ApiModelProperty(value="总销售实际完成")
	@JsonProperty("total_finish")
	private Integer totalFinish;


	@ApiModelProperty(value="18SA销售目标值")
	@JsonProperty("eighteen_target")
	private Integer eighteenTarget;


	@ApiModelProperty(value="18SA销售实际完成")
	@JsonProperty("eighteen_finish")
	private Integer eighteenFinish;


	@ApiModelProperty(value="重点品牌销售目标值")
	@JsonProperty("key_target")
	private Integer keyTarget;


	@ApiModelProperty(value="重点品牌销售实际值")
	@JsonProperty("key_finish")
	private Integer keyFinish;


	@ApiModelProperty(value="服纺销售目标值")
	@JsonProperty("textile_target")
	private Integer textileTarget;


	@ApiModelProperty(value="服纺销售实际值")
	@JsonProperty("textile_finish")
	private Integer textileFinish;
}