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

    /***门店订货*/
    STORE_ORDER("STORE_ORDER", "门店订货"),

    //TODO 字段未确定
    ;

    private String value;
    private String desc;

    ErpRequestPayTransactionTypeEnum(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

}
