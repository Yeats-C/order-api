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

    /***tob余额支付*/
    TYPE_7(7, "7", "配送订货-toB配送订单支付"),
    /***在线微信*/
    TYPE_11(11, "11", "直送订货-toB直送订单支付"),
    /***在线支付*/
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
