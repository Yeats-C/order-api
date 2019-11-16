package com.aiqin.mgs.order.api.component.enums;

import lombok.Getter;

/**
 * 购物车商品类型
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/11/15 11:09
 */
@Getter
public enum CartProductTypeEnum {

    /***直送*/
    DIRECT_SEND(1, "1", "直送"),
    /***配送*/
    DISTRIBUTION(2, "2", "配送"),
    /***货架*/
    STORAGE_RACK(3, "3", "货架");

    private Integer code;
    private String value;
    private String desc;

    CartProductTypeEnum(Integer code, String value, String desc) {
        this.code = code;
        this.value = value;
        this.desc = desc;
    }

    public static CartProductTypeEnum getEnum(Integer code) {
        for (CartProductTypeEnum type : CartProductTypeEnum.values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return null;
    }

    public static CartProductTypeEnum getEnum(String value) {
        for (CartProductTypeEnum type : CartProductTypeEnum.values()) {
            if (type.getValue().equals(value)) {
                return type;
            }
        }
        return null;
    }


    public static String getEnumDesc(Integer code) {
        for (CartProductTypeEnum type : CartProductTypeEnum.values()) {
            if (type.getCode().equals(code)) {
                return type.getDesc();
            }
        }
        return "";
    }

    public static String getEnumDesc(String value) {
        for (CartProductTypeEnum type : CartProductTypeEnum.values()) {
            if (type.getValue().equals(value)) {
                return type.getDesc();
            }
        }
        return "";
    }
}
