package com.aiqin.mgs.order.api.component.enums;

import lombok.Getter;

/**
 * 逻辑状态枚举类
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/11/14 17:39
 */
@Getter
public enum YesOrNoEnum {

    /***逻辑-是*/
    YES(1, "1", "是"),
    /***逻辑-否*/
    NO(0, "0", "否");

    /***数字类型状态*/
    private Integer code;
    /***字符串类型状态*/
    private String value;
    /***状态描述*/
    private String desc;

    YesOrNoEnum(Integer code, String value, String desc) {
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
    public static YesOrNoEnum getEnum(Object object) {
        if (object != null) {
            for (YesOrNoEnum item :
                    YesOrNoEnum.values()) {
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
            YesOrNoEnum anEnum = getEnum(object.toString());
            if (anEnum != null) {
                return anEnum.getDesc();
            }
        }
        return "";
    }


    /**
     * 校验值是否存在
     *
     * @param object
     * @return
     */
    public static boolean exist(Object object) {
        if (object != null) {
            YesOrNoEnum anEnum = getEnum(object.toString());
            if (anEnum != null) {
                return true;
            }
        }
        return false;
    }
}
