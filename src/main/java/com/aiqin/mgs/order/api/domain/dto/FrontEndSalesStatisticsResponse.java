package com.aiqin.mgs.order.api.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author by wpp25
 * @Classname FrontEndSalesStatistics
 * @Description 前台销售统计响应数据
 * @Date 2020/2/15 14:07
 */
@Data
@ApiModel("前台销售统计响应数据")
public class FrontEndSalesStatisticsResponse {

    @JsonProperty("store_id")
    @ApiModelProperty("门店id")
    private String storeId;

    @JsonProperty("sku_code")
    @ApiModelProperty("sku码")
    private String skuCode;

    @JsonProperty("sku_name")
    @ApiModelProperty("sku名")
    private String skuName;

    @JsonProperty("category_id")
    @ApiModelProperty("分类id")
    private String categoryId;

    @JsonProperty("category_name")
    @ApiModelProperty("分类名称")
    private String categoryName;

    @JsonProperty("sale_total_count")
    @ApiModelProperty("销售总数")
    private Long saleTotalCount;

    @JsonProperty("sale_total_amount")
    @ApiModelProperty("销售总额")
    private Long saleTotalAmount;

    @JsonProperty("price_unit")
    @ApiModelProperty("价格单位 0：分 1： 元 默认为分")
    private Integer priceUnit;

    @JsonProperty("sku_unit")
    @ApiModelProperty("sku单位 eg：包  听等等")
    private String skuUnit;

}
