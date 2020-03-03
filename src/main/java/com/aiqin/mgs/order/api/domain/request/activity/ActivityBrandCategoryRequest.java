package com.aiqin.mgs.order.api.domain.request.activity;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author csf
 */
@Data
@ApiModel("活动调用供应链品牌品类接口请求参数")
public class ActivityBrandCategoryRequest implements Serializable {

    @ApiModelProperty("品牌编码集合")
    @JsonProperty("brand_ids")
    private List<String> brandIds;

    @ApiModelProperty("排除品牌编码集合")
    @JsonProperty("exclude_brand_ids")
    private List<String> excludeBrandIds;

    @ApiModelProperty("品类编码集合")
    @JsonProperty("category_codes")
    private List<String> categoryCodes;

    @ApiModelProperty("排除品类编码集合")
    @JsonProperty("exclude_category_codes")
    private List<String> excludeCategoryCodes;

    @ApiModelProperty("品类查询全部还是启用,0为启用,4为全部")
    @JsonProperty("category_status")
    private String categoryStatus;

    @ApiModelProperty("品牌名称查询参数")
    @JsonProperty("name")
    private String name;



}
