package com.aiqin.mgs.order.api.component.enums.activity;

import lombok.Getter;

/**
 * 活动类型枚举类
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2020/2/18 17:23
 */
@Getter
public enum ActivityTypeEnum {
    /***满减*/
    TYPE_1(1, "1", "满减"),
    TYPE_2(2, "2", "满赠"),
    TYPE_3(3, "3", "折扣"),
    TYPE_4(4, "4", "返点"),
    TYPE_5(5, "5", "特价"),
    TYPE_6(6, "6", "整单"),
    TYPE_7(7, "7", "买赠"),
    ;

    /***数字类型状态*/
    private Integer code;
    /***字符串类型状态*/
    private String value;
    /***状态描述*/
    private String desc;

    ActivityTypeEnum(Integer code, String value, String desc) {
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
    public static ActivityTypeEnum getEnum(Object object) {
        if (object != null) {
            for (ActivityTypeEnum item :
                    ActivityTypeEnum.values()) {
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
            ActivityTypeEnum anEnum = getEnum(object.toString());
            if (anEnum != null) {
                return anEnum.getDesc();
            }
        }
        return "";
    }
}
