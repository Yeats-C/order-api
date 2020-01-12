package com.aiqin.mgs.order.api.component.enums.pay;

import lombok.Getter;

@Getter
public enum ErpRequestPayOrderTypeEnum {

    /***配送tob*/
    TYPE_14(14, "14", "配送tob"),
    /***直送tob*/
    TYPE_2(2, "2", "直送tob"),
    ;

    private Integer code;
    private String value;
    private String desc;

    ErpRequestPayOrderTypeEnum(Integer code, String value, String desc) {
        this.code = code;
        this.value = value;
        this.desc = desc;
    }
}
