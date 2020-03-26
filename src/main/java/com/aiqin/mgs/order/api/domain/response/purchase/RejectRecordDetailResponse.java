package com.aiqin.mgs.order.api.domain.response.purchase;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class RejectRecordDetailResponse {

    @ApiModelProperty(value = "sku编码")
    @JsonProperty("sku_code")
    private String skuCode;

    @ApiModelProperty(value = "sku名称")
    @JsonProperty("sku_name")
    private String skuName;

    @ApiModelProperty(value = "品类")
    @JsonProperty("category_name")
    private String categoryName;

    @ApiModelProperty(value = "品牌")
    @JsonProperty("brand_name")
    private String brandName;

    @ApiModelProperty(value = "商品类型 0商品 1赠品 2实物返")
    @JsonProperty("product_type")
    private Integer productType;

    @ApiModelProperty(value = "规格")
    @JsonProperty("product_spec")
    private String productSpec;

    @ApiModelProperty(value = "颜色")
    @JsonProperty("color_name")
    private String colorName;

    @ApiModelProperty(value = "型号")
    @JsonProperty("model_code")
    private String modelCode;

    @ApiModelProperty(value = "商品数量")
    @JsonProperty("product_count")
    private Long productCount;

    @ApiModelProperty(value = "最小单位数量")
    @JsonProperty("total_count")
    private Long totalCount;

    @ApiModelProperty(value = "单位")
    @JsonProperty("unit_name")
    private String unitName;

    @ApiModelProperty(value = "税率")
    @JsonProperty("tax_rate")
    private BigDecimal taxRate;

    @ApiModelProperty(value = "含税单价")
    @JsonProperty("tax_amount")
    private BigDecimal taxAmount;

    @ApiModelProperty(value = "含税总价")
    @JsonProperty("total_tax_amount")
    private BigDecimal totalTaxAmount;

    @ApiModelProperty(value = "实际数量")
    @JsonProperty("actual_total_count")
    private Long actualTotalCount;

    @ApiModelProperty(value = "实际金额")
    @JsonProperty("actual_total_tax_amount")
    private BigDecimal actualTotalTaxAmount;

    @ApiModelProperty(value = "行号")
    @JsonProperty("line_code")
    private Integer lineCode;

}
