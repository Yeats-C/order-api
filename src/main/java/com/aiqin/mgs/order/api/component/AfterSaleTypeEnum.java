package com.aiqin.mgs.order.api.component;

import lombok.Getter;

/**
 * 售后类型枚举类
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/11/11 15:20
 */
@Getter
public enum AfterSaleTypeEnum {

    /***赔偿*/
    AFTER_SALE_TYPE_0(0, "0", "赔偿"),
    /***退货*/
    AFTER_SALE_TYPE_1(1, "1", "退货"),
    /***换货*/
    AFTER_SALE_TYPE_2(2, "2", "换货");

    private Integer code;
    private String value;
    private String desc;

    AfterSaleTypeEnum(Integer code, String value, String desc) {
        this.code = code;
        this.value = value;
        this.desc = desc;
    }

    public static AfterSaleTypeEnum getEnum(Integer code) {
        for (AfterSaleTypeEnum type : AfterSaleTypeEnum.values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return null;
    }

    public static AfterSaleTypeEnum getEnum(String value) {
        for (AfterSaleTypeEnum type : AfterSaleTypeEnum.values()) {
            if (type.getValue().equals(value)) {
                return type;
            }
        }
        return null;
    }


    public static String getEnumDesc(Integer code) {
        for (AfterSaleTypeEnum type : AfterSaleTypeEnum.values()) {
            if (type.getCode().equals(code)) {
                return type.getDesc();
            }
        }
        return "";
    }

    public static String getEnumDesc(String value) {
        for (AfterSaleTypeEnum type : AfterSaleTypeEnum.values()) {
            if (type.getValue().equals(value)) {
                return type.getDesc();
            }
        }
        return "";
    }
}
