package com.aiqin.mgs.order.api.component.enums;

import lombok.Getter;

/**
 * 商品本品赠品枚举类
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/11/15 17:33
 */
@Getter
public enum ProductGiftEnum {

    /***逻辑-是*/
    PRODUCT(1, "1", "本品"),
    /***逻辑-否*/
    GIFT(2, "2", "赠品");

    private Integer code;
    private String value;
    private String desc;

    ProductGiftEnum(Integer code, String value, String desc) {
        this.code = code;
        this.value = value;
        this.desc = desc;
    }

    public static ProductGiftEnum getEnum(Integer code) {
        for (ProductGiftEnum type : ProductGiftEnum.values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return null;
    }

    public static ProductGiftEnum getEnum(String value) {
        for (ProductGiftEnum type : ProductGiftEnum.values()) {
            if (type.getValue().equals(value)) {
                return type;
            }
        }
        return null;
    }


    public static String getEnumDesc(Integer code) {
        for (ProductGiftEnum type : ProductGiftEnum.values()) {
            if (type.getCode().equals(code)) {
                return type.getDesc();
            }
        }
        return "";
    }

    public static String getEnumDesc(String value) {
        for (ProductGiftEnum type : ProductGiftEnum.values()) {
            if (type.getValue().equals(value)) {
                return type.getDesc();
            }
        }
        return "";
    }

}
