package com.aiqin.mgs.order.api.domain.po.order;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 子订单费用明细
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/12/11 20:18
 */
@Data
@JsonInclude(JsonInclude.Include.ALWAYS)
public class ItemOrderFee {

    @ApiModelProperty(value = "子订单商品价值：（子订单分销价求和）元")
    @JsonProperty("total_money")
    private BigDecimal totalMoney;

    @ApiModelProperty(value = "子订单活动优惠：活动优惠金额求和）元")
    @JsonProperty("activity_money")
    private BigDecimal activityMoney;

    @ApiModelProperty(value = "服纺券抵减:（子订单服纺券抵扣分摊求和）元")
    @JsonProperty("suit_coupon_money")
    private BigDecimal suitCouponMoney;

    @ApiModelProperty(value = "A品券抵减：（子订单A品券抵扣分摊求和）元")
    @JsonProperty("top_coupon_money")
    private BigDecimal topCouponMoney;

    @ApiModelProperty(value = "实付金额（元）")
    @JsonProperty("pay_money")
    private BigDecimal payMoney;

    @ApiModelProperty(value = "使用赠品额度：（使用赠品额度求和）元")
    @JsonProperty("used_gift_quota")
    private BigDecimal usedGiftQuota;


}
