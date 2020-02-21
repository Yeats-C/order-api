package com.aiqin.mgs.order.api.domain.request.activity;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author csf
 */
@Data
@ApiModel("活动校验查询请求参数")
public class ActivityParameterRequest implements Serializable {

    /**活动id*/
    @ApiModelProperty(value = "活动id")
    @JsonProperty("activity_id")
    private String activityId="";

    @ApiModelProperty(value = "门店id")
    @JsonProperty("store_id")
    private String storeId;

    @ApiModelProperty(value = "商品编码")
    @JsonProperty("sku_code")
    private String skuCode="";

    /***商品品牌编码*/
    @ApiModelProperty(value = "商品品牌编码")
    @JsonProperty("product_brand_code")
    private String productBrandCode;

    /***商品品类编码*/
    @ApiModelProperty(value = "商品品类编码")
    @JsonProperty("product_category_code")
    private String productCategoryCode;

}
