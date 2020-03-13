package com.aiqin.mgs.order.api.component.enums.cart;

import lombok.Getter;

@Getter
public enum ErpCartLineStatusEnum {

    /***正常*/
    NORMAL(1, "1", "正常"),
    /***库存不足*/
    UNDER_STOCK(2, "2", "库存不足"),
    /***禁用*/
    NOT_ALLOWED(3, "3", "禁用"),
    ;

    /***数字类型状态*/
    private Integer code;
    /***字符串类型状态*/
    private String value;
    /***状态描述*/
    private String desc;

    ErpCartLineStatusEnum(Integer code, String value, String desc) {
        this.code = code;
        this.value = value;
        this.desc = desc;
    }
}
