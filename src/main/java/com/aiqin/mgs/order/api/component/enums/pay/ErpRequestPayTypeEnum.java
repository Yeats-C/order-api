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
public enum ErpRequestPayTypeEnum {

    /***tob余额支付*/
    PAY_10(10, "10", "tob余额支付"),
    /***在线微信*/
    PAY_0(0, "0", "在线微信"),
    /***在线支付*/
    PAY_1(1, "1", "在线支付"),
    ;

    private Integer code;
    private String value;
    private String desc;

    ErpRequestPayTypeEnum(Integer code, String value, String desc) {
        this.code = code;
        this.value = value;
        this.desc = desc;
    }

}
