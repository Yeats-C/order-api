package com.aiqin.mgs.order.api.component.enums;

import lombok.Getter;

/**
 * 订单附属级别
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/11/15 15:42
 */
@Getter
public enum ErpOrderLevelEnum {

    /***主订单*/
    PRIMARY(0, "0", "主订单"),
    /***子订单*/
    SECONDARY(1, "1", "子订单");

    /***数字类型状态*/
    private Integer code;
    /***字符串类型状态*/
    private String value;
    /***状态描述*/
    private String desc;

    ErpOrderLevelEnum(Integer code, String value, String desc) {
        this.code = code;
        this.value = value;
        this.desc = desc;
    }

    /**
     * 根据值获取枚举类
     *
     * @param object
     * @return
     */
    public static ErpOrderLevelEnum getEnum(Object object) {
        if (object != null) {
            for (ErpOrderLevelEnum item :
                    ErpOrderLevelEnum.values()) {
                if (item.getValue().equals(object.toString())) {
                    return item;
                }
            }
        }
        return null;
    }

    /**
     * 根据枚举类的值获取枚举的描述
     *
     * @param object
     * @return
     */
    public static String getEnumDesc(Object object) {
        if (object != null) {
            ErpOrderLevelEnum anEnum = getEnum(object.toString());
            if (anEnum != null) {
                return anEnum.getDesc();
            }
        }
        return "";
    }
    
}
