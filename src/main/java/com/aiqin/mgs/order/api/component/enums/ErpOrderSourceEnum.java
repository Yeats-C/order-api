package com.aiqin.mgs.order.api.component.enums;

import lombok.Getter;

@Getter
public enum ErpOrderSourceEnum {

    /***运营erp*/
    ERP(1, "1", "运营ERP"),
    /***爱掌柜*/
    STORE(2, "2", "爱掌柜");

    /***数字类型状态*/
    private Integer code;
    /***字符串类型状态*/
    private String value;
    /***状态描述*/
    private String desc;

    ErpOrderSourceEnum(Integer code, String value, String desc) {
        this.code = code;
        this.value = value;
        this.desc = desc;
    }
}
