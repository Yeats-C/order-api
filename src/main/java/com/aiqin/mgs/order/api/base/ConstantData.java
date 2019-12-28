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
//    Integer couponType = 2;
    Integer COUPON_TYPE = 2;
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
    /**
     * 退货单状态(供应链使用):4-等待退货验收，5-等待退货入库 11-退货完成
     */
    Integer returnOrderStatusComplete = 11;
    /**
     * 退款状态，0-未退款、1-已退款
     */
    Integer refundStatus = 1;
    /**
     * 支付流水--支付类型 1:付款  2:退款
     */
    Integer payTypeRefund = 2;
    /**
     * 退货单--退款方式 1:现金 2:微信 3:支付宝 4:银联 5:退到加盟商账户
     */
    Integer returnMoneyType = 5;

    /**
     * 退货单--退款方式 1:现金 2:微信 3:支付宝 4:银联 5:退到加盟商账户
     */
    String returnMoneyTypeName = "退到加盟商账户";

}
