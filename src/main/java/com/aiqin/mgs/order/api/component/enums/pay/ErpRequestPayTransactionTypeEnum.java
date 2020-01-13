package com.aiqin.mgs.order.api.component.enums.pay;

import lombok.Getter;

/**
 * 订单支付交易类型
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/12/27 9:59
 */
@Getter
public enum ErpRequestPayTransactionTypeEnum {

    /****/
    STORE_WITHDRAWAL("STORE_WITHDRAWAL", "门店提现"),
    DELIVERY_REPLENISHMENT("DELIVERY_REPLENISHMENT", "配送补货"),
    DIRECT_DELIVERY_REPLENISHMENT("DIRECT_DELIVERY_REPLENISHMENT", "直送补货"),
    ACCOUNT_FREEZING("ACCOUNT_FREEZING", "账户冻结"),
    FINANCIAL_TRANSFER_OUT("FINANCIAL_TRANSFER_OUT", "财务转出"),
    FINANCIAL_CREDIT_CANCELLATION("FINANCIAL_CREDIT_CANCELLATION", "财务取消授信"),
    LOGISTICS_PAYMENT("LOGISTICS_PAYMENT", "物流费支付"),
    FIRST_DELIVERY("FIRST_DELIVERY", "首单配送"),
    FIRST_DIRECT_DELIVERY("FIRST_DIRECT_DELIVERY", "首单直送"),
    ONLINE_RECHARGE("ONLINE_RECHARGE", "在线充值"),
    AFTER_SALE_RETURNS("AFTER_SALE_RETURNS", "售后退货"),
    STORE_CANCEL_ORDER("STORE_CANCEL_ORDER", "门店取消订单"),
    DELIVER_GOODS_DEDUCT("DELIVER_GOODS_DEDUCT", "发货冲减"),
    OUT_OF_STOCK_CANCELLATION("OUT_OF_STOCK_CANCELLATION", "缺货取消"),
    ACCOUNT_UNFREEZING("ACCOUNT_UNFREEZING", "账户解冻"),
    FINANCIAL_TRANSFER_IN("FINANCIAL_TRANSFER_IN", "财务转入"),
    FINANCIAL_CREDIT("FINANCIAL_CREDIT", "财务授信"),
    ;

    private String value;
    private String desc;

    ErpRequestPayTransactionTypeEnum(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

}
