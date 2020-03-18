package com.aiqin.mgs.order.api.domain.request.activity;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel("品类和品牌返回vo")
@Data
public class ProductCategoryAndBrandResponse2 {

    @ApiModelProperty(value = "品牌集合")
    @JsonProperty("query_product_brand_resp_VO")
    private List<QueryProductBrandRespVO> queryProductBrandRespVO;

    @ApiModelProperty(value = "品类集合")
    @JsonProperty("product_category_resp_VO_List")
    private List<ProductCategoryRespVO> productCategoryRespVOList;


}