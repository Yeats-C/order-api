package com.aiqin.mgs.order.api.component.enums.activity;

import lombok.Getter;

/**
 * 活动优惠单位枚举类
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2020/2/18 17:29
 */
@Getter
public enum ActivityRuleUnitEnum {

    /***按数量*/
    BY_NUM(1, "1", "按数量"),
    /***按金额*/
    BY_MONEY(2, "2", "按金额"),
    ;

    /***数字类型状态*/
    private Integer code;
    /***字符串类型状态*/
    private String value;
    /***状态描述*/
    private String desc;

    ActivityRuleUnitEnum(Integer code, String value, String desc) {
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
    public static ActivityRuleUnitEnum getEnum(Object object) {
        if (object != null) {
            for (ActivityRuleUnitEnum item :
                    ActivityRuleUnitEnum.values()) {
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
            ActivityRuleUnitEnum anEnum = getEnum(object.toString());
            if (anEnum != null) {
                return anEnum.getDesc();
            }
        }
        return "";
    }

}
