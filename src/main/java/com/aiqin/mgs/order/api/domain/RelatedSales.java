package com.aiqin.mgs.order.api.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel("关联销售")
public class RelatedSales {

    @ApiModelProperty(value = "id")
    @JsonProperty("id")
    private Long id;

    @ApiModelProperty(value = "销售品类名称")
    @JsonProperty("category_name")
    private String salseCategoryName;

    @ApiModelProperty(value = "销售品类ID")
    @JsonProperty("category_code")
    private String salseCategoryId;

    @ApiModelProperty(value = "优先推荐sku")
    @JsonProperty("first_sku")
    private String firstSku;

    @ApiModelProperty(value = "优先推荐sku名称")
    @JsonProperty("first_sku_name")
    private String firstSkuName;


    @ApiModelProperty(value = "其次推荐sku")
    @JsonProperty("secondly_sku")
    private String secondlySku;


    @ApiModelProperty(value = "其次推荐sku名称")
    @JsonProperty("secondly_sku_name")
    private String secondlySkuName;

    @ApiModelProperty(value = "最次推荐sku")
    @JsonProperty("last_sku")
    private String lastSku;

    @ApiModelProperty(value = "最次推荐sku名称")
    @JsonProperty("last_sku_name")
    private String lastSkuName;

    @ApiModelProperty(value = "生效状态 0:生效 1：失效")
    @JsonProperty("status")
    private Integer status;

    @ApiModelProperty(value = "创建时间")
    @JsonProperty("create_time")
    private Date createTime;

    @ApiModelProperty(value = "修改时间")
    @JsonProperty("update_time")
    private Date updateTime;

}