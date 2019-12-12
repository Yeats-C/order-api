package com.aiqin.mgs.order.api.domain.po.order;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 订单费用
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/12/11 20:18
 */
@Data
public class ErpOrderFee extends ErpOrderBase {

    /***订单费用id*/
    private String feeId;
    /***关联订单id*/
    private String orderId;
    /***支付单id*/
    private String payId;
    /***支付状态*/
    private Integer payStatus;
    /***订单总额*/
    private BigDecimal totalMoney;
    /***活动优惠金额*/
    private BigDecimal activityMoney;
    /***服纺券优惠金额*/
    private BigDecimal suitCouponMoney;
    /***A品券优惠金额*/
    private BigDecimal topCouponMoney;
    /***实付金额*/
    private BigDecimal payMoney;
    /***返还物流券金额*/
    private BigDecimal goodsCoupon;

    /***订单支付*/
    private ErpOrderPay orderPay;
}
