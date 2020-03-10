package com.aiqin.mgs.order.api.domain.response.report;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * description: ReportCategoryResponse
 * date: 2020/3/7 17:20
 * author: hantao
 * version: 1.0
 */
@Data
@ApiModel("接收一级品类实体类")
public class ReportCategoryResponse implements Serializable {

    @ApiModelProperty(value = "品类编码")
    private String categoryCode;

    @ApiModelProperty(value = "品类名称")
    private String categoryName;

    @ApiModelProperty(value = "层级")
    private Integer level;

}
