package com.aiqin.mgs.order.api.domain.response.report;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * description: ProvinceAreaResponse
 * date: 2020/3/2 20:04
 * author: hantao
 * version: 1.0
 */
@Data
@ApiModel("查询省实体类")
public class ProvinceAreaResponse implements Serializable {

    @ApiModelProperty(value = "省编码")
    @JsonProperty("area_id")
    private String areaId;

    @ApiModelProperty(value = "省编码")
    @JsonProperty("area_name")
    private String areaName;

}
