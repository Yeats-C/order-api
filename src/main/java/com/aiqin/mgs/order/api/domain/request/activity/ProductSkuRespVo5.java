package com.aiqin.mgs.order.api.domain.request.activity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author knight.xie
 * @version 1.0
 * @className ProductSkuRespVo5
 * @date 2020/02/18 11:53
 */
@ApiModel("SKU信息返回--knight.xie")
@Data
public class ProductSkuRespVo5 {

    @ApiModelProperty(value = "sku编号")
    private String skuCode;

    @ApiModelProperty("sku名称")
    private String skuName;

    @ApiModelProperty("商品编码")
    private String spuCode;

    @ApiModelProperty("商品名称")
    private String spuName;

    @ApiModelProperty("商品品牌code")
    private String productBrandCode;

    @ApiModelProperty("商品品牌")
    private String productBrandName;

    @ApiModelProperty("商品品类code")
    private String productCategoryCode;

    @ApiModelProperty("商品品类名称")
    private String productCategoryName;

    @ApiModelProperty("商品属性code")
    private String productPropertyCode;

    @ApiModelProperty("商品属性名称")
    private String productPropertyName;

    @ApiModelProperty("分销价格")
    private BigDecimal price;

    @ApiModelProperty("分销规格")
    private String spec;

    @ApiModelProperty("库存")
    private Long skuStock;

    @ApiModelProperty("封面图")
    private String itroImages;

}
