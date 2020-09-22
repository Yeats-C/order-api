package com.aiqin.mgs.order.api.domain.copartnerArea;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 描述:
 *
 * @author huangzy
 * @create 2020-02-13
 */
@ApiModel("经营区域列表")
@Data
public class CopartnerAreaList{
	
	private String id;
	
	@ApiModelProperty(value = "经营区域ID")
    @JsonProperty("copartner_area_id")
    private String copartnerAreaId;
    
	@ApiModelProperty(value = "经营区域名称")
    @JsonProperty("copartner_area_name")
    private String copartnerAreaName;
	
	@ApiModelProperty(value = "管理归属")
    @JsonProperty("copartner_area_company")
    private String copartnerAreaCompany;
	
	@ApiModelProperty(value = "管理层级 1:一级 2：二级")
    @JsonProperty("copartner_area_level")
    private Integer copartnerAreaLevel;
	
	@ApiModelProperty(value = "上一层级名称")
    @JsonProperty("copartner_area_name_up")
    private String copartnerAreaNameUp;
	
	@ApiModelProperty(value = "包含门店数")
    @JsonProperty("store_amount")
    private Integer storeAmount;

    @ApiModelProperty(value = "上一层级是否存在，不存在为1")
    @JsonProperty("copartner_area_up")
    private Integer copartnerAreaUp;
}
