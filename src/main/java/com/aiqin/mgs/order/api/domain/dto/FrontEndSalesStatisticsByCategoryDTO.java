package com.aiqin.mgs.order.api.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author by wpp25
 * @Classname FrontEndSalesStatistics
 * @Description 前台销售根据分类信息统计响应数据dto
 * @Date 2020/2/15 14:07
 */
@Data
@ApiModel("前台销售根据分类信息统计响应数据dto")
public class FrontEndSalesStatisticsByCategoryDTO {

    @JsonProperty("store_id")
    @ApiModelProperty("门店id")
    private String storeId;

    @JsonProperty("sku_dto_list")
    @ApiModelProperty("sku_dto集合")
    private List<FrontEndSalesStatisticsBySkuDTO> skuDTOList;

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

}
