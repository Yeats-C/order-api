package com.aiqin.mgs.order.api.domain.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * description: CouponShareRequest
 * date: 2020/2/24 15:47
 * author: hantao
 * version: 1.0
 */
@Data
@ApiModel("A品卷分摊")
public class CouponShareRequest {

    @ApiModelProperty(value = "行号")
    @JsonProperty("line_code")
    private Long lineCode;

    @ApiModelProperty(value = "sku编码")
    @JsonProperty("sku_code")
    private String skuCode;

    @ApiModelProperty(value = "数量")
    @JsonProperty("product_count")
    private Long productCount;

    @ApiModelProperty(value = "行总价（元）")
    @JsonProperty("total_product_amount")
    private BigDecimal totalProductAmount;

    @ApiModelProperty(value = "商品属性编码")
    @JsonProperty("product_property_code")
    private String productPropertyCode;

    @ApiModelProperty(value = "优惠分摊总金额（分摊后金额）（元）")
    @JsonProperty("total_preferential_amount")
    private BigDecimal totalPreferentialAmount;

    @ApiModelProperty(value = "分摊后单价（元）")
    @JsonProperty("preferential_amount")
    private BigDecimal preferentialAmount;

    @ApiModelProperty(value = "本行A品卷优惠金额")
    @JsonProperty("apin_coupon_amount")
    private BigDecimal apinCouponAmount;

}
