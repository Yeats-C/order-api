package com.aiqin.mgs.order.api.domain.request.activity;

import com.aiqin.mgs.order.api.domain.Activity;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

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
    @JsonProperty("sku_code")
    private String skuCode;

    @ApiModelProperty("sku名称")
    @JsonProperty("sku_name")
    private String skuName;

    @ApiModelProperty("商品编码")
    @JsonProperty("spu_code")
    private String spuCode;

    @ApiModelProperty("商品名称")
    @JsonProperty("spu_name")
    private String spuName;

    @ApiModelProperty("商品品牌code")
    @JsonProperty("product_brand_code")
    private String productBrandCode;

    @ApiModelProperty("商品品牌")
    @JsonProperty("product_brand_name")
    private String productBrandName;

    @ApiModelProperty("商品品类code")
    @JsonProperty("product_category_code")
    private String productCategoryCode;

    @ApiModelProperty("商品品类名称")
    @JsonProperty("product_category_name")
    private String productCategoryName;

    @ApiModelProperty("商品属性code")
    @JsonProperty("product_property_code")
    private String productPropertyCode;

    @ApiModelProperty("商品属性名称")
    @JsonProperty("product_property_name")
    private String productPropertyName;

    @ApiModelProperty("分销价格")
    @JsonProperty("price")
    private BigDecimal price;

    @ApiModelProperty("分销规格")
    @JsonProperty("spec")
    private String spec;

    @ApiModelProperty("库存")
    @JsonProperty("sku_stock")
    private Long skuStock;

    @ApiModelProperty("封面图")
    @JsonProperty("itro_images")
    private String itroImages="";

    @ApiModelProperty("交易倍数")
    @JsonProperty("zero_removal_coefficient")
    private Integer zeroRemovalCoefficient;

    @ApiModelProperty("最大订购数量")
    @JsonProperty("max_order_num")
    private Integer maxOrderNum;

    @ApiModelProperty("供货渠道类别code")
    @JsonProperty("categories_supply_channels_code")
    private String categoriesSupplyChannelsCode;

    @ApiModelProperty("货渠道类别名称")
    @JsonProperty("categories_supply_channels_name")
    private String categoriesSupplyChannelsName;

    @ApiModelProperty("活动List")
    @JsonProperty("activity_list")
    private List<Activity> activityList;

    @ApiModelProperty("加入购物车数量")
    @JsonProperty("cart_num")
    private Integer cartNum;

    @ApiModelProperty("门店库存数量")
    @JsonProperty("store_stock_sku_num")
    private Integer storeStockSkuNum;

    @ApiModelProperty("门店库存数量描述1 有货—— 总部库存大于10 ，2缺货——总库库存为0 ，3 库存紧张 ——总库库存小于等于10")
    @JsonProperty("store_stock_explain")
    private String storeStockExplain;

    @ApiModelProperty("是否能使用A品卷 0否 1是")
    @JsonProperty("coupon_rule")
    private Integer couponRule=0;



}
