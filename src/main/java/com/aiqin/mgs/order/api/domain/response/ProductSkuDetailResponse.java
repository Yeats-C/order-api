package com.aiqin.mgs.order.api.domain.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.math.BigDecimal;

@Data
@JsonInclude(JsonInclude.Include.ALWAYS)
public class ProductSkuDetailResponse {

    /***商品编码*/
    private String productCode;
    /***商品名称*/
    private String productName;
    /***skuCode*/
    private String skuCode;
    /***skuName*/
    private String skuName;
    /***供应商编码*/
    private String supplyUnitCode;
    /***供应商名称*/
    private String supplyUnitName;
    /***图片地址*/
    private String productPicturePath;
    /***规格*/
    private String spec;
    /***颜色编码*/
    private String colorCode;
    /***颜色名称*/
    private String colorName;
    /***型号*/
    private String modelNumber;
    /***单位编码*/
    private String unitCode;
    /***单位名称*/
    private String unitName;
    /***单价*/
    private BigDecimal priceTax;
    /***条形码*/
    private String barCode;
    /***税率*/
    private BigDecimal outputTaxRate;
    /***供货渠道编码*/
    private String categoriesSupplyChannelsCode;
    /***供货渠道名称*/
    private String categoriesSupplyChannelsName;
    /***商品属性编码*/
    private String productPropertyCode;
    /***商品属性名称*/
    private String productPropertyName;
}
