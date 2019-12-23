package com.aiqin.mgs.order.api.base;

import java.math.BigDecimal;

/**
 * description: ConstantData
 * date: 2019/12/19 11:06
 * author: hantao
 * version: 1.0
 */
public interface ConstantData {

    /**
     * A品卷类型
     */
    Integer couponType = 2;
    /**
     * 优惠券名称--A品券
     */
    String aCouponName = "A品卷";
    /**
     * A品券面值--100
     */
    BigDecimal nominalValue= BigDecimal.valueOf(100.00);
    /**
     * A品券发放申请工作流，流程key
     */
    String applyCoupon01 = "ERP_COUPON01";
    /**
     * 12-退款完成
     */
    Integer returnOrderSuccess = 12;

    /**
     * 退货单审核状态：01-待审核
     */
    Integer returnOrderStatusWait = 1;

}
