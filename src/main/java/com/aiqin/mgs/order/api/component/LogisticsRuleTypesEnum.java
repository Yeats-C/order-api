package com.aiqin.mgs.order.api.component;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum LogisticsRuleTypesEnum {

    LOGISTICS_PIECE(0,"件"),
    LOGISTICS_YUAN(1,"元");

    private Integer code;
    private String  name;

    public Integer getCode(){
        return code;
    }
}
