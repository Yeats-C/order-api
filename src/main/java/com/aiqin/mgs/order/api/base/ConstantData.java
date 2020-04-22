package com.aiqin.mgs.order.api.base;

import java.math.BigDecimal;

/**
 * description: ConstantData
 * date: 2019/12/19 11:06
 * author: hantao
 * version: 1.0
 */
public class ConstantData {

    private ConstantData() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * A品卷类型
     */
    public static final Integer COUPON_TYPE = 2;
    /**
     * 优惠券名称--A品券
     */
    public static final String COUPON_NAME_A = "A品卷";
    /**
     * A品券面值--100
     */
    public static final BigDecimal NOMINAL_VALUE= BigDecimal.valueOf(100.00);
    /**
     * A品券发放申请工作流，流程key
     */
    public static final String APPLY_COUPON01_KEY = "ERP_COUPON01";
    /**
     * 12-退款完成
     */
    public static final Integer RETURN_ORDER_SUCCESS = 12;

    /**
     * 退货单审核状态：01-待审核
     */
    public static final Integer RETURN_ORDER_STATUS_WAIT = 1;
    /**
     * 退货单状态(供应链使用):4-等待退货验收，5-等待退货入库 11-退货完成
     */
    public static final Integer RETURN_ORDER_STATUS_COMPLETE = 11;
    /**
     * 退款状态，0-未退款、1-已退款
     */
    public static final Integer REFUND_STATUS = 1;
    /**
     * 支付流水--支付类型 1:付款  2:退款
     */
    public static final Integer PAY_TYPE_REFUND = 2;
    /**
     * 退货单--退款方式 1:现金 2:微信 3:支付宝 4:银联 5:退到加盟商账户
     */
    public static final Integer RETURN_MONEY_TYPE = 5;

    /**
     * 退货单--退款方式 1:现金 2:微信 3:支付宝 4:银联 5:退到加盟商账户
     */
    public static final String RETURN_MONEY_TYPE_NAME = "退到加盟商账户";

    /**
     * 退货处理--修改商品详情
     */
    public static final String RETURN_ORDER_DETAIL = "修改商品详情";

    /**
     * 操作人--系统操作
     */
    public static final String SYS_OPERTOR = "系统操作";

    /**
     * 首单赠送市值使用超额申请工作流，流程key
     */
    public static final String FIRST_SINGLE_EXCESS_KEY = "ERP_FIRST_SINGLE_EXCESS";

}
