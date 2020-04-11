package com.aiqin.mgs.order.api.component.enums;

import lombok.Getter;

/**
 * 商品本品赠品枚举
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/11/15 17:33
 */
@Getter
public enum ErpProductGiftEnum {

    /***商品*/
    PRODUCT(0, "0", "商品"),
    /***赠品*/
    GIFT(1, "1", "赠品"),
    JIFEN(2, "2", "赠品积分所选商品");

    private Integer code;
    private String value;
    private String desc;

    ErpProductGiftEnum(Integer code, String value, String desc) {
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
    public static ErpProductGiftEnum getEnum(Object object) {
        if (object != null) {
            for (ErpProductGiftEnum item :
                    ErpProductGiftEnum.values()) {
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
            ErpProductGiftEnum anEnum = getEnum(object.toString());
            if (anEnum != null) {
                return anEnum.getDesc();
            }
        }
        return "";
    }

}
