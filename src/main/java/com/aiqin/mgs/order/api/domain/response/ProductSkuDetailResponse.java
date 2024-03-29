package com.aiqin.mgs.order.api.domain.response;

import com.aiqin.mgs.order.api.domain.request.product.BatchRespVo;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.ALWAYS)
public class ProductSkuDetailResponse {

    /***商品编码*/
    @JsonProperty("product_code")
    private String productCode;
    /***商品名称*/
    @JsonProperty("product_name")
    private String productName;
    /***skuCode*/
    @JsonProperty("sku_code")
    private String skuCode;
    /***skuName*/
    @JsonProperty("sku_name")
    private String skuName;
    /***供应商编码*/
    @JsonProperty("supply_unit_code")
    private String supplyUnitCode;
    /***供应商名称*/
    @JsonProperty("supply_unit_name")
    private String supplyUnitName;
    /***图片地址*/
    @JsonProperty("product_picture_path")
    private String productPicturePath;
    /***规格*/
    @JsonProperty("spec")
    private String spec;
    /***颜色编码*/
    @JsonProperty("color_code")
    private String colorCode;
    /***颜色名称*/
    @JsonProperty("color_name")
    private String colorName;
    /***型号*/
    @JsonProperty("model_number")
    private String modelNumber;
    /***单位编码*/
    @JsonProperty("unit_code")
    private String unitCode;
    /***单位名称*/
    @JsonProperty("unit_name")
    private String unitName;
    /***单价*/
    @JsonProperty("price_tax")
    private BigDecimal priceTax;
    /***条形码*/
    @JsonProperty("bar_code")
    private String barCode;
    /***税率*/
    @JsonProperty("output_tax_rate")
    private BigDecimal outputTaxRate;
    /***供货渠道编码*/
    @JsonProperty("categories_supply_channels_code")
    private String categoriesSupplyChannelsCode;
    /***供货渠道名称*/
    @JsonProperty("categories_supply_channels_name")
    private String categoriesSupplyChannelsName;
    /***商品属性编码*/
    @JsonProperty("product_property_code")
    private String productPropertyCode;
    /***商品属性名称*/
    @JsonProperty("product_property_name")
    private String productPropertyName;
    /***商品品牌名称*/
    @JsonProperty("product_brand_name")
    private String productBrandName;
    /***商品品牌编码*/
    @JsonProperty("product_brand_code")
    private String productBrandCode;
    /***商品品类名称*/
    @JsonProperty("product_category_name")
    private String productCategoryName;
    /***商品品类编码*/
    @JsonProperty("product_category_code")
    private String productCategoryCode;
    /***SKU整包装信息返回*/
    @JsonProperty("productSkuBoxPackings")
    private List<ProductSkuBoxPackings> productSkuBoxPackings;

    @ApiModelProperty("批次信息")
    @JsonProperty(value = "batch_list")
    private List<BatchRespVo> batchList;

    /***爱亲采购价*/
    @JsonProperty("price_tax1")
    private BigDecimal priceTax1;
}
