package com.aiqin.mgs.order.api.component.enums.cart;

import lombok.Getter;


@Getter
public enum ErpProductGiftGiveTypeEnum {

    /***赠完即止*/
    TYPE_1(1, "1", "赠完即止"),
    ;

    /***数字类型状态*/
    private Integer code;
    /***字符串类型状态*/
    private String value;
    /***状态描述*/
    private String desc;

    ErpProductGiftGiveTypeEnum(Integer code, String value, String desc) {
        this.code = code;
        this.value = value;
        this.desc = desc;
    }

}