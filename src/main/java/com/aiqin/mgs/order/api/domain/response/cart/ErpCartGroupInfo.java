package com.aiqin.mgs.order.api.domain.response.cart;

import com.aiqin.mgs.order.api.domain.Activity;
import com.aiqin.mgs.order.api.domain.ActivityRule;
import com.aiqin.mgs.order.api.domain.po.cart.ErpOrderCartInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@ApiModel("购物车商品楼层")
@Data
public class ErpCartGroupInfo {

    @ApiModelProperty(value = "是否是有活动的楼层  0:不是，作为普通商品楼层解析;  1:是，作为有活动的楼层解析")
    @JsonProperty("has_activity")
    private Integer hasActivity;

    @ApiModelProperty(value = "活动主体信息")
    @JsonProperty("activity")
    private Activity activity;

    @ApiModelProperty(value = "活动当前命中规则，如果为空，则表示当前商品数量和金额没有满足活动的条件")
    @JsonProperty("activity_rule")
    private ActivityRule activityRule;

    @ApiModelProperty(value = "活动的最小梯度")
    @JsonProperty("first_activity_rule")
    private ActivityRule firstActivityRule;

    @ApiModelProperty(value = "楼层总的分销金额汇总")
    @JsonProperty("group_amount")
    private BigDecimal groupAmount;

    @ApiModelProperty(value = "楼层总的活动价金额汇总")
    @JsonProperty("group_activity_amount")
    private BigDecimal groupActivityAmount;

    @ApiModelProperty(value = "楼层总的活动优惠减少的金额汇总")
    @JsonProperty("group_activity_discount_amount")
    private BigDecimal groupActivityDiscountAmount;

    @ApiModelProperty(value = "楼层总的本品数量")
    @JsonProperty("group_product_quantity")
    private Integer groupProductQuantity;

    @ApiModelProperty(value = "楼层总的赠品数量")
    @JsonProperty("group_gift_quantity")
    private Integer groupGiftQuantity;

    @ApiModelProperty(value = "楼层本品分销价汇总")
    @JsonProperty("group_product_amount")
    private BigDecimal groupProductAmount;

    @ApiModelProperty(value = "楼层A品本品均摊后金额汇总")
    @JsonProperty("group_top_coupon_max_total")
    private BigDecimal groupTopCouponMaxTotal;

    @ApiModelProperty(value = "楼层的商品本品列表")
    @JsonProperty("cart_product_list")
    private List<ErpOrderCartInfo> cartProductList;

    @ApiModelProperty(value = "楼层的商品赠品列表")
    @JsonProperty("cart_gift_list")
    private List<ErpOrderCartInfo> cartGiftList;


}
