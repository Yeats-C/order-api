package com.aiqin.mgs.order.api.domain.po.order;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 订单导出实体
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/12/9 9:49
 */
@Data
@JsonInclude(JsonInclude.Include.ALWAYS)
public class DownloadOrderInfoVo {

    @ApiModelProperty(value = "门店编码")
    @JsonProperty("store_code")
    private String storeCode;

    @ApiModelProperty(value = "门店名称")
    @JsonProperty("store_name")
    private String storeName;

    @ApiModelProperty(value="所属合伙人公司")
    @JsonProperty("copartner_area_name")
    private String copartnerAreaName;

    @ApiModelProperty(value="零售主管id")
    @JsonProperty("supervisor_id")
    private String supervisorId;

    @ApiModelProperty(value="零售主管名称")
    @JsonProperty("supervisor_name")
    private String supervisorName;

    @ApiModelProperty(value = "商品类型 0商品（本品） 1赠品 2兑换赠品")
    @JsonProperty("product_type")
    private Integer productType;

    @ApiModelProperty(value = "商品品牌编码")
    @JsonProperty("product_brand_code")
    private String productBrandCode;

    @ApiModelProperty(value = "商品品牌名称")
    @JsonProperty("product_brand_name")
    private String productBrandName;

    @ApiModelProperty(value = "商品品类编码")
    @JsonProperty("product_category_code")
    private String productCategoryCode;

    @ApiModelProperty(value = "商品品类名称")
    @JsonProperty("product_category_name")
    private String productCategoryName;

    @ApiModelProperty(value = "商品属性编码")
    @JsonProperty("product_property_code")
    private String productPropertyCode;

    @ApiModelProperty(value = "商品属性名称")
    @JsonProperty("product_property_name")
    private String productPropertyName;

    @ApiModelProperty(value = "sku编码")
    @JsonProperty("sku_code")
    private String skuCode;

    @ApiModelProperty(value = "sku名称")
    @JsonProperty("sku_name")
    private String skuName;

    @ApiModelProperty(value = "单价[分销价]（元）")
    @JsonProperty("product_amount")
    private BigDecimal productAmount;

    @ApiModelProperty(value = "活动价（元）")
    @JsonProperty("activity_price")
    private BigDecimal activityPrice;

    @ApiModelProperty(value = "订货数量")
    @JsonProperty("product_count")
    private Long productCount;

    @ApiModelProperty(value = "实发数量")
    @JsonProperty("actual_product_count")
    private Long actualProductCount;

    @ApiModelProperty(value = "分摊后单价（元）")
    @JsonProperty("preferential_amount")
    private BigDecimal preferentialAmount;

    @ApiModelProperty(value = "本行A品券优惠总额度")
    @JsonProperty("top_coupon_discount_amount")
    private BigDecimal topCouponDiscountAmount;

    @ApiModelProperty(value = "活动优惠总金额,包括活动优惠和优惠券优惠（元）")
    @JsonProperty("total_acivity_amount")
    private BigDecimal totalAcivityAmount;

    @ApiModelProperty(value = "使用赠品额度【订单详情展示字段】")
    @JsonProperty("used_gift_quota")
    private BigDecimal usedGiftQuota;

    @ApiModelProperty(value = "关联主订单号  如果是主订单，该字段存自己的订单号")
    @JsonProperty("main_order_code")
    private String mainOrderCode;

    @ApiModelProperty(value = "订单类型编码")
    @JsonProperty("order_type_code")
    private String orderTypeCode;

    @ApiModelProperty(value = "订单类型名称")
    @JsonProperty("order_type_name")
    private String orderTypeName;

    @ApiModelProperty(value = "订单类别编码")
    @JsonProperty("order_category_code")
    private String orderCategoryCode;

    @ApiModelProperty(value = "订单类别名称")
    @JsonProperty("order_category_name")
    private String orderCategoryName;

    @ApiModelProperty(value = "订单状态")
    @JsonProperty("order_status")
    private Integer orderStatus;

    public BigDecimal getUsedGiftQuota(){
        if(this.productType==2){
            return this.productAmount.multiply(BigDecimal.valueOf(this.productCount));
        }else {
            return BigDecimal.ZERO;
        }
    }

}
