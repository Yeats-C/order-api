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

    /***首单配送*/
    ORDER_TYPE_1(1, "1", "首单配送"),
    /***首单赠送*/
    ORDER_TYPE_2(2, "2", "首单赠送"),
    /***首单货架*/
    ORDER_TYPE_3(3, "3", "首单货架"),
    /***货架补货*/
    ORDER_TYPE_4(4, "4", "货架补货"),
    /***配送补货*/
    ORDER_TYPE_5(5, "5", "配送补货"),
    /***游乐设备*/
    ORDER_TYPE_6(6, "6", "游乐设备"),
    /***首单直送*/
    ORDER_TYPE_7(7, "7", "首单直送"),
    /***直送补货*/
    ORDER_TYPE_8(8, "8", "直送补货");

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

    public static boolean exist(Integer code) {
        for (OrderTypeEnum type : OrderTypeEnum.values()) {
            if (type.getCode().equals(code)) {
                return true;
            }
        }
        return false;
    }

    public static boolean exist(String value) {
        for (OrderTypeEnum type : OrderTypeEnum.values()) {
            if (type.getValue().equals(value)) {
                return true;
            }
        }
        return false;
    }
}

