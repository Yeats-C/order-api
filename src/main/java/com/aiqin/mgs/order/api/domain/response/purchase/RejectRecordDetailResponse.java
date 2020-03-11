package com.aiqin.mgs.order.api.domain.response.purchase;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class RejectRecordDetailResponse {

    @ApiModelProperty(value = "")
    @JsonProperty("sku_code")
    private String skuCode;

    @ApiModelProperty(value = "")
    @JsonProperty("sku_name")
    private String skuName;

    @ApiModelProperty(value = "")
    @JsonProperty("category_name")
    private String categoryName;

    @ApiModelProperty(value = "")
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

    @ApiModelProperty(value = "厂商sku")
    @JsonProperty("factory_sku_code")
    private String factorySkuCode;

    @ApiModelProperty(value = "型号")
    @JsonProperty("model_number")
    private String modelNumber;

    @ApiModelProperty(value = "商品数量")
    @JsonProperty("product_count")
    private String productCount;

    @ApiModelProperty(value = "最小单位数量")
    @JsonProperty("single_count")
    private String singleCount;

    @ApiModelProperty(value = "单位")
    @JsonProperty("unit_name")
    private String unitName;

    @ApiModelProperty(value = "税率")
    @JsonProperty("tax_rate")
    private BigDecimal taxRate;

    @ApiModelProperty(value = "含税单价")
    @JsonProperty("product_amount")
    private BigDecimal productAmount;

    @ApiModelProperty(value = "含税总价")
    @JsonProperty("product_total_amount")
    private BigDecimal productTotalAmount;

    @ApiModelProperty(value = "wms 传回来的实际数量")
    @JsonProperty("actual_count")
    private Integer actualCount;

    @ApiModelProperty(value = "wms 传回来的实际金额")
    @JsonProperty("actual_amount")
    private BigDecimal actualAmount;

}
