package com.aiqin.mgs.order.api.component.enums;

import lombok.Getter;

/**
 * 订单类型枚举类
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/11/12 17:00
 */
@Getter
public enum OrderTypeEnum {

    //TODO 状态待定

    /***状态1*/
    ORDER_STATUS_1(1, "1", "状态1"),
    /***状态2*/
    ORDER_STATUS_2(2, "2", "状态2");

    private Integer code;
    private String value;
    private String desc;

    OrderTypeEnum(Integer code, String value, String desc) {
        this.code = code;
        this.value = value;
        this.desc = desc;
    }

    public static OrderTypeEnum getEnum(Integer code) {
        for (OrderTypeEnum type : OrderTypeEnum.values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return null;
    }

    public static OrderTypeEnum getEnum(String value) {
        for (OrderTypeEnum type : OrderTypeEnum.values()) {
            if (type.getValue().equals(value)) {
                return type;
            }
        }
        return null;
    }


    public static String getEnumDesc(Integer code) {
        for (OrderTypeEnum type : OrderTypeEnum.values()) {
            if (type.getCode().equals(code)) {
                return type.getDesc();
            }
        }
        return "";
    }

    public static String getEnumDesc(String value) {
        for (OrderTypeEnum type : OrderTypeEnum.values()) {
            if (type.getValue().equals(value)) {
                return type.getDesc();
            }
        }
        return "";
    }
}

