package com.aiqin.mgs.order.api.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("订单商品从表")
@Data
public class OrderListProduct {
    private String id;

    @ApiModelProperty(value = "订单商品ID")
    @JsonProperty("order_product_id")
    private String orderProductId;

    @ApiModelProperty(value = "订单编号")
    @JsonProperty("order_code")
    private String orderCode;

    @ApiModelProperty(value = "商品sku码")
    @JsonProperty("sku_code")
    private String skuCode;

    @ApiModelProperty(value = "商品名称")
    @JsonProperty("sku_name")
    private String skuName;

    @ApiModelProperty(value = "商品数量")
    @JsonProperty("product_number")
    private Integer productNumber;

    @ApiModelProperty(value = "参与活动编号")
    @JsonProperty("activity_code")
    private String activityCode;

    @ApiModelProperty(value = "订单参与活动类型 (6-条件类型满减，7-条件类型满赠，8-条件类型折扣，9-条件类型特价)")
    @JsonProperty("activity_type")
    private Integer activityType;

    @ApiModelProperty(value = "参与活动名称")
    @JsonProperty("activity_name")
    private String activityName;

    @ApiModelProperty(value = "是否赠品(0:不是赠品 1:是赠品)")
    @JsonProperty("gift")
    private Integer gift;

    @ApiModelProperty(value = "赠送赠品商品ID")
    @JsonProperty("original_product_id")
    private String originalProductId;

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

    @ApiModelProperty(value = "实发数量")
    @JsonProperty("actual_deliver_num")
    private Integer actualDeliverNum;

    @ApiModelProperty(value = "商品价格（单品合计成交价)")
    @JsonProperty("product_price")
    private Long productPrice;

    @ApiModelProperty(value = "商品单价")
    @JsonProperty("original_product_price")
    private Long originalProductPrice;

//    @ApiModelProperty(value = "商品总额（单价*数量）")
//    @JsonProperty("subtotal_price")
//    private Long subtotalPrice;

    @ApiModelProperty(value = "优惠额度抵扣金额（单品合计）")
    @JsonProperty("discount_money")
    private Long discountMoney;

//    @ApiModelProperty(value = "促销活动抵扣金额（单品合计）")
//    @JsonProperty("promotion_discount")
//    private Long promotionDiscount;

    @ApiModelProperty(value = "总价")
    @JsonProperty("amount")
    private Long amount;

    @ApiModelProperty(value = "图片地址")
    @JsonProperty("picture_url")
    private String pictureUrl;

    @ApiModelProperty(value = "优惠分摊")
    @JsonProperty("preferential_allocation")
    private Long preferentialAllocation;

    @ApiModelProperty(value = "退货数量")
    @JsonProperty("return_num")
    private Integer returnNum;

    @ApiModelProperty(value = "供应单位编码")
    @JsonProperty("supply_company_code")
    private String supplyCompanyCode;

    @ApiModelProperty(value = "供应单位名称")
    @JsonProperty("supply_company_name")
    private String supplyCompanyName;

//    @ApiModelProperty(value = "优惠额度编号")
//    @JsonProperty("discount_code")
//    private String discountCode;

    @ApiModelProperty(value = "是否使用优惠额度,1-使用了优惠额度，0-没使用")
    @JsonProperty("use_discount_amount")
    private Integer useDiscountAmount;

    @ApiModelProperty(value = "优惠额度信息（json）")
    @JsonProperty("discount_amount_info")
    private String discountAmountInfo;

    @ApiModelProperty(value = "商品单位重量，不存入数据库也不进行json序列化")
    @JSONField(serialize = false)
    private Integer weight;

    @ApiModelProperty(value = "商品类型，0-正常品，1-效期品")
    @JsonProperty("product_type")
    private Integer productType;
}