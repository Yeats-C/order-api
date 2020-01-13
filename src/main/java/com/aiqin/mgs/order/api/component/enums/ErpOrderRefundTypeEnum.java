package com.aiqin.mgs.order.api.component.enums;

import lombok.Getter;

/**
 * 订单退款类型枚举类
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2020/1/7 11:41
 */
@Getter
public enum ErpOrderRefundTypeEnum {

    /***订单取消退款*/
    ORDER_CANCEL(1, "1", "订单取消退款"),
    ;

    private Integer code;
    private String value;
    private String desc;

    ErpOrderRefundTypeEnum(Integer code, String value, String desc) {
        this.code = code;
        this.value = value;
        this.desc = desc;
    }

}
