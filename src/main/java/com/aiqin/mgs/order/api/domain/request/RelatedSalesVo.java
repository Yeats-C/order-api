package com.aiqin.mgs.order.api.domain.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * description: RelatedSalesVo
 * date: 2020/2/14 11:43
 * author: hantao
 * version: 1.0
 */
@Data
@ApiModel("关联销售查询")
public class RelatedSalesVo {

    @ApiModelProperty(value = "销售品类ID")
    @JsonProperty("salse_category_id")
    private String salseCategoryId;

    @ApiModelProperty(value = "生效状态 0:生效 1：失效")
    @JsonProperty("status")
    private Integer status;

}
