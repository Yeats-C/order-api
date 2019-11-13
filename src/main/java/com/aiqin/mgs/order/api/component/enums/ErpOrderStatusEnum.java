package com.aiqin.mgs.order.api.component.enums;

import lombok.Getter;

/**
 * 订单状态枚举
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/11/12 16:53
 */
@Getter
public enum ErpOrderStatusEnum {

    //TODO 状态待定

    /***状态1*/
    ORDER_STATUS_1(1, "1", "状态1"),
    /***状态2*/
    ORDER_STATUS_2(2, "2", "状态2");

    private Integer code;
    private String value;
    private String desc;

    ErpOrderStatusEnum(Integer code, String value, String desc) {
        this.code = code;
        this.value = value;
        this.desc = desc;
    }

    public static ErpOrderStatusEnum getEnum(Integer code) {
        for (ErpOrderStatusEnum type : ErpOrderStatusEnum.values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return null;
    }

    public static ErpOrderStatusEnum getEnum(String value) {
        for (ErpOrderStatusEnum type : ErpOrderStatusEnum.values()) {
            if (type.getValue().equals(value)) {
                return type;
            }
        }
        return null;
    }


    public static String getEnumDesc(Integer code) {
        for (ErpOrderStatusEnum type : ErpOrderStatusEnum.values()) {
            if (type.getCode().equals(code)) {
                return type.getDesc();
            }
        }
        return "";
    }

    public static String getEnumDesc(String value) {
        for (ErpOrderStatusEnum type : ErpOrderStatusEnum.values()) {
            if (type.getValue().equals(value)) {
                return type.getDesc();
            }
        }
        return "";
    }
}
