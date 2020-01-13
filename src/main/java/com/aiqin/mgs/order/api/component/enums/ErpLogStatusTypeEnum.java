package com.aiqin.mgs.order.api.component.enums;

import lombok.Getter;

@Getter
public enum ErpLogStatusTypeEnum {


    /***启用*/
    USING(0, "0", "启用"),
    /***禁用*/
    FORBIDDEN(1, "1", "禁用"),
    ;

    private Integer code;
    private String value;
    private String desc;

    ErpLogStatusTypeEnum(Integer code, String value, String desc) {
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
    public static ErpLogStatusTypeEnum getEnum(Object object) {
        if (object != null) {
            for (ErpLogStatusTypeEnum item :
                    ErpLogStatusTypeEnum.values()) {
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
            ErpLogStatusTypeEnum anEnum = getEnum(object.toString());
            if (anEnum != null) {
                return anEnum.getDesc();
            }
        }
        return "";
    }
}
