package com.aiqin.mgs.order.api.domain.request.activity;

import com.aiqin.mgs.order.api.domain.Activity;
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

    @ApiModelProperty("活动List")
    private List<Activity> activityList;

    @ApiModelProperty("加入购物车数量")
    private Integer cartNum;

    @ApiModelProperty("门店库存数量")
    private Integer storeStockSkuNum;

    @ApiModelProperty("门店库存数量描述1 有货—— 总部库存大于10 ，2缺货——总库库存为0 ，3 库存紧张 ——总库库存小于等于10")
    private String storeStockExplain;

}
