package com.aiqin.mgs.order.api.component;

public enum LogisticsRuleEnum {

    SINGLE_BUY_QUANTITY(0,"单品购买数量免运费"),
    SINGLE_BUY_AMOUNT(1,"单品购买金额免运费"),
    CONSTITUTE_BUY_QUANTITY(2,"累计购买数量免运费"),
    CONSTITUTE_BUY_AMOUNT(3,"累计购买金额免运费");

    private Integer code;
    private String  name;

    private LogisticsRuleEnum(Integer code,String name){
        this.code = code;
        this.name = name;
    }

    public Integer getkey(){
        return code;
    }

    public String getName(){
        return name;
    }
}
