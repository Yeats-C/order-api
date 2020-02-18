package com.aiqin.mgs.order.api.domain.copartnerArea;

import com.aiqin.mgs.order.api.base.PagesRequest;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 描述:
 *
 * @author huangzy
 * @create 2020-02-13
 */
@ApiModel("上级合伙人")
@Data
public class CopartnerAreaUp{
	
	@ApiModelProperty(value = "经营区域ID")
    @JsonProperty("copartner_area_id")
    private String copartnerAreaId;
    
	@ApiModelProperty(value = "经营区域名称")
    @JsonProperty("copartner_area_name")
    private String copartnerAreaName;
	
}
