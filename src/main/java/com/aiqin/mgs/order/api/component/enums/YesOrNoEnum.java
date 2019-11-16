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

    public static YesOrNoEnum getEnum(Integer code) {
        for (YesOrNoEnum type : YesOrNoEnum.values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return null;
    }

    public static YesOrNoEnum getEnum(String value) {
        for (YesOrNoEnum type : YesOrNoEnum.values()) {
            if (type.getValue().equals(value)) {
                return type;
            }
        }
        return null;
    }


    public static String getEnumDesc(Integer code) {
        for (YesOrNoEnum type : YesOrNoEnum.values()) {
            if (type.getCode().equals(code)) {
                return type.getDesc();
            }
        }
        return "";
    }

    public static String getEnumDesc(String value) {
        for (YesOrNoEnum type : YesOrNoEnum.values()) {
            if (type.getValue().equals(value)) {
                return type.getDesc();
            }
        }
        return "";
    }
}
