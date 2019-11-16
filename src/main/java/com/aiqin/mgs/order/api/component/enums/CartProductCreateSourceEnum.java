package com.aiqin.mgs.order.api.component.enums;

import lombok.Getter;

/**
 * 购物车商品添加来源
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/11/15 17:30
 */
@Getter
public enum CartProductCreateSourceEnum {

    /***运维中心ERP*/
    ERP(1, "1", "运维中心ERP"),
    /***爱掌柜*/
    STORE(2, "2", "爱掌柜");

    private Integer code;
    private String value;
    private String desc;

    CartProductCreateSourceEnum(Integer code, String value, String desc) {
        this.code = code;
        this.value = value;
        this.desc = desc;
    }

    public static CartProductCreateSourceEnum getEnum(Integer code) {
        for (CartProductCreateSourceEnum type : CartProductCreateSourceEnum.values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return null;
    }

    public static CartProductCreateSourceEnum getEnum(String value) {
        for (CartProductCreateSourceEnum type : CartProductCreateSourceEnum.values()) {
            if (type.getValue().equals(value)) {
                return type;
            }
        }
        return null;
    }

    public static String getEnumDesc(Integer code) {
        for (CartProductCreateSourceEnum type : CartProductCreateSourceEnum.values()) {
            if (type.getCode().equals(code)) {
                return type.getDesc();
            }
        }
        return "";
    }

    public static String getEnumDesc(String value) {
        for (CartProductCreateSourceEnum type : CartProductCreateSourceEnum.values()) {
            if (type.getValue().equals(value)) {
                return type.getDesc();
            }
        }
        return "";
    }
}
