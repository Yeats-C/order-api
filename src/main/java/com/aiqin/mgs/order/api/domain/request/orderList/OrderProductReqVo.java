package com.aiqin.mgs.order.api.domain.request.orderList;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * OrderProductReqVo
 *
 * @author zhangtao
 * @createTime 2019-01-18
 * @description
 */
@Data
public class OrderProductReqVo implements Serializable {

    private static final long serialVersionUID = -1223728764746461137L;

    @ApiModelProperty(value = "商品sku码")
    @JsonProperty("sku_code")
    private String skuCode;

    @ApiModelProperty(value = "商品名称")
    @JsonProperty("sku_name")
    private String skuName;

    @ApiModelProperty(value = "商品数量")
    @JsonProperty("product_number")
    private Integer productNumber = 0;

    @ApiModelProperty(value = "参与活动编号")
    @JsonProperty("activity_code")
    private String activityCode;

    @ApiModelProperty(value = "参与活动名称")
    @JsonProperty("activity_name")
    private String activityName;

    @ApiModelProperty(value = "是否赠品(0:不是赠品 1:是赠品)")
    @JsonProperty("gift")
    private Integer gift = 0;

    @ApiModelProperty(value = "赠送赠品商品SKU")
    @JsonProperty("original_product_sku")
    private String originalProductSku;

    @ApiModelProperty(value = "规格")
    @JsonProperty("specifications")
    private String specifications;

    @ApiModelProperty(value = "单位")
    @JsonProperty("unit")
    private String unit;

    @ApiModelProperty(value = "颜色code")
    @JsonProperty("color_code")
    private String colorCode;

    @ApiModelProperty(value = "颜色名称")
    @JsonProperty("color_name")
    private String colorName;

    @ApiModelProperty(value = "型号")
    @JsonProperty("model_number")
    private String modelNumber;

    @ApiModelProperty(value = "商品价格（单品合计成交价)")
    @JsonProperty("product_price")
    private Long productPrice = 0L;

    @ApiModelProperty(value = "商品单价")
    @JsonProperty("original_product_price")
    private Long originalProductPrice = 0L;

    @ApiModelProperty(value = "优惠额度抵扣金额（单品合计）")
    @JsonProperty("discount_money")
    private Long discountMoney = 0L;

    @ApiModelProperty(value = "总价")
    @JsonProperty("amount")
    private Long amount = 0L;

    @ApiModelProperty(value = "优惠分摊")
    @JsonProperty("preferential_allocation")
    private Long preferentialAllocation = 0L;

    @ApiModelProperty(value = "图片地址")
    @JsonProperty("picture_url")
    private String pictureUrl;

    @ApiModelProperty(value = "供应单位编码")
    @JsonProperty("supply_company_code")
    private String supplyCompanyCode;

    @ApiModelProperty(value = "供应单位名称")
    @JsonProperty("supply_company_name")
    private String supplyCompanyName;

    @ApiModelProperty(value = "是否使用优惠额度,1-使用了优惠额度，0-没使用")
    @JsonProperty("use_discount_amount")
    private Integer useDiscountAmount = 0;

    @ApiModelProperty(value = "商品单位重量")
    @JsonProperty("weight")
    private Integer weight = 0;

    @JsonProperty("product_type")
    @ApiModelProperty("商品类型，1-正常品，0-效期品")
    private Integer productType=1;

    @ApiModelProperty(value = "优惠额度信息")
    @JsonProperty("discount_amount_info")
    private List<DiscountAmountInfo> discountAmountInfo;
}
