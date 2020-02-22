package com.aiqin.mgs.order.api.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 商品信息
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/11/22 17:11
 */
@Data
@JsonInclude(JsonInclude.Include.ALWAYS)
public class ProductInfo {

    /***商品编码*/
    private String spuCode;
    /***商品名称*/
    private String spuName;
    /***skuCode*/
    private String skuCode;
    /***skuName*/
    private String skuName;
    /***供应商编码*/
    private String supplierCode;
    /***供应商名称*/
    private String supplierName;
    /***图片地址*/
    private String pictureUrl;
    /***规格*/
    private String productSpec;
    /***颜色编码*/
    private String colorCode;
    /***颜色名称*/
    private String colorName;
    /***型号*/
    private String modelCode;
    /***单位编码*/
    private String unitCode;
    /***单位名称*/
    private String unitName;
    /***单价*/
    private BigDecimal price;
    /***条形码*/
    private String barCode;
    /***税率*/
    private BigDecimal taxRate;
    /***商品属性编码*/
    private String productPropertyCode;
    /***商品属性名称*/
    private String productPropertyName;
    /***商品品牌名称*/
    private String productBrandName;
    /***商品品牌编码*/
    private String productBrandCode;
    /***商品品类名称*/
    private String productCategoryName;
    /***商品品类编码*/
    private String productCategoryCode;
    /***包装毛重*/
    private BigDecimal boxGrossWeight;
    /***箱子体积*/
    private BigDecimal boxVolume;

}
