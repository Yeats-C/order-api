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
@ApiModel("活动调用供应链查询skupage接口封装品类请求参数")
public class ActivityCategoryRequest implements Serializable {


    /***商品品类编码*/
    @ApiModelProperty(value = "商品品类编码")
    @JsonProperty("product_category_code")
    private String productCategoryCode;

    /***品类级别*/
    @ApiModelProperty(value = "品类级别")
    @JsonProperty("level")
    private Integer level;


}
