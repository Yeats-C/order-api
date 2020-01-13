package com.aiqin.mgs.order.api.component.enums.pay;

import lombok.Getter;
/**
 * 发起支付参数支付来源
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/12/27 10:15
 */
@Getter
public enum ErpRequestPayOriginTypeEnum {

    /****/
//    TOC_POS(1,"toC-POS消费(包含所有订单状态下的支付)"),
//    TOC_MICRO_MART(2,"toC-微商城消费(包含所有订单状态下的支付)"),
//    TOC_WEB(3,"toC-web消费(包含所有订单状态下的支付)"),
//    TOB_CANCEL_ORDER(4,"配送订货-toB-客户取消退货"),
//    TOB_LESS_CANCEL(5,"配送订货-toB-缺货取消退货"),
//    TOB_RETURN(6,"配送订货-toB-售后退货"),
//    TOB_PAY(7,"配送订货-toB配送订单支付"),
//    DIRECT_SEND_TOB_CANCEL(8,"直送订货-toB-客户取消退货"),
//    DIRECT_SEND_TOB_LESS_CANCEL(9,"直送订货-toB-缺货取消退货"),
//    DIRECT_SEND_TOB_RETURN(10,"直送订货-toB-售后退货"),
//    DIRECT_SEND_TOB_PAY(11,"直送订货-toB直送订单支付"),
//    WITHDRAW_CASH_ONLINE(12,"配送账户-线上提现"),
//    FEE(13,"配送账户-手续费"),
//    RECHARGE(14,"配送账户-充值"),
//    DIRECT_SEND_WITHDRAW_CASH_ONLINE(15,"直送账户-线上提现"),
//    DIRECT_SEND_FEE(16,"直送账户-手续费"),
//    DIRECT_SEND_RECHARGE(17,"直送账户-充值"),
//    TOC_RECHARGE_POS(18,"toC-POS储值(包含所有订单状态下的支付)"),
//    TOC_RECHARGE_WEB(19,"toC-WEB储值(包含所有订单状态下的支付)"),
//
//    TOC_REFUNDMON_WEB(20,"toC-WEB仅退款(包含所有订单状态下的支付)"),
//    TOC_REFUNDMONGOOD_WEB(21,"toC-WEB退款退货(包含所有订单状态下的支付)"),
//    TOC_REFUNDMON_POS(22,"toC-POS仅退款(包含所有订单状态下的支付)"),
//    TOC_REFUNDMONGOOD_POS(23,"toC-POS退款退货(包含所有订单状态下的支付)"),
//    TOB_LOGISTICS_PAY(24,"TOB-物流支付(包含所有订单状态下的支付)");

//    private Integer code;
//
//    private String desc;
//
//    ErpRequestPayOriginTypeEnum(Integer code, String desc) {
//        this.code = code;
//        this.desc = desc;
//    }

        /***配送和货架支付订单费用 */
    TYPE_7(7, "7", "配送订货-toB配送订单支付"),
    /***直送支付订单费用*/
    TYPE_11(11, "11", "直送订货-toB直送订单支付"),
    /***支付物流费用*/
    TYPE_24(24, "24", "TOB-物流支付(包含所有订单状态下的支付)"),
    ;

    private Integer code;
    private String value;
    private String desc;

    ErpRequestPayOriginTypeEnum(Integer code, String value, String desc) {
        this.code = code;
        this.value = value;
        this.desc = desc;
    }

}
