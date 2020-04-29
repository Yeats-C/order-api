package com.aiqin.mgs.order.api.domain.po.order;

import com.aiqin.mgs.order.api.component.enums.pay.ErpPayStatusEnum;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单费用
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/12/11 20:18
 */
@Data
@JsonInclude(JsonInclude.Include.ALWAYS)
public class ErpOrderFee {

    @ApiModelProperty(value = "主键")
    @JsonProperty("id")
    private Long id;

    @ApiModelProperty(value = "费用id")
    @JsonProperty("fee_id")
    private String feeId;

    @ApiModelProperty(value = "关联订单id")
    @JsonProperty("关联订单id")
    private String orderId;

    @ApiModelProperty(value = "支付单id")
    @JsonProperty("pay_id")
    private String payId;

    @ApiModelProperty(value = "费用支付状态")
    @JsonProperty("pay_status")
    private Integer payStatus;

    @ApiModelProperty(value = "支付状态描述")
    @JsonProperty("pay_status_desc")
    private String payStatusDesc;

    @ApiModelProperty(value = "订单总额（元）")
    @JsonProperty("total_money")
    private BigDecimal totalMoney;

    @ApiModelProperty(value = "活动优惠金额（元）")
    @JsonProperty("activity_money")
    private BigDecimal activityMoney;

    @ApiModelProperty(value = "服纺券优惠金额（元）")
    @JsonProperty("suit_coupon_money")
    private BigDecimal suitCouponMoney;

    @ApiModelProperty(value = "A品券优惠金额（元）")
    @JsonProperty("top_coupon_money")
    private BigDecimal topCouponMoney;

    @ApiModelProperty(value = "实付金额（元）")
    @JsonProperty("pay_money")
    private BigDecimal payMoney;

    @ApiModelProperty(value = "返还物流券金额（元）")
    @JsonProperty("goods_coupon")
    private BigDecimal goodsCoupon;

    @ApiModelProperty(value = "使用赠品额度")
    @JsonProperty("used_gift_quota")
    private BigDecimal usedGiftQuota;

    @ApiModelProperty(value = "关联A品券编码")
    @JsonProperty("top_coupon_codes")
    private String topCouponCodes;

    @ApiModelProperty(value = "创建时间")
    @JsonProperty("create_time")
    private Date createTime;

    @ApiModelProperty(value = "创建人id")
    @JsonProperty("create_by_id")
    private String createById;

    @ApiModelProperty(value = "创建人姓名")
    @JsonProperty("create_by_name")
    private String createByName;

    @ApiModelProperty(value = "更新时间")
    @JsonProperty("update_time")
    private Date updateTime;

    @ApiModelProperty(value = "修改人id")
    @JsonProperty("update_by_id")
    private String updateById;

    @ApiModelProperty(value = "修改人姓名")
    @JsonProperty("update_by_name")
    private String updateByName;

    @ApiModelProperty(value = "数据状态 1有效 0删除")
    @JsonProperty("status")
    private Integer status;

    @ApiModelProperty(value = "服纺券作废金额（元）")
    @JsonProperty("nullify_suit_coupon_money")
    private BigDecimal nullifySuitCouponMoney;

    @ApiModelProperty(value = "A品券作废金额（元）")
    @JsonProperty("nullify_top_coupon_money")
    private BigDecimal nullifyTopCouponMoney;

    @ApiModelProperty(value = "发放赠品额度")
    @JsonProperty("complimentary_amount")
    private BigDecimal complimentaryAmount;

    /**
     * 非数据库字段
     */
    @ApiModelProperty(value = "支付流水号")
    @JsonProperty("pay_code")
    private String payCode;

    @ApiModelProperty(value = "关联支付单")
    @JsonProperty("order_pay")
    private ErpOrderPay orderPay;

    public String getPayStatusDesc() {
        return ErpPayStatusEnum.getEnumDesc(payStatus);
    }
}
