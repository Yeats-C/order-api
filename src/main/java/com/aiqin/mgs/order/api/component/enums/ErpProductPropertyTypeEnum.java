package com.aiqin.mgs.order.api.component.enums;

import lombok.Getter;

/**
 * 商品属性枚举类
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2020/1/11 16:46
 */
@Getter
public enum ErpProductPropertyTypeEnum {

    /***商品属性*/
    TYPE_1(1, "1", "18A", true),
    TYPE_2(2, "2", "18B", false),
    TYPE_3(3, "3", "C品", false),
    TYPE_4(4, "4", "D品", false),
    TYPE_5(5, "5", "其他", false),
    TYPE_6(6, "6", "18A", true),
    ;

    private Integer code;
    private String value;
    private String desc;
    /***使用A品券*/
    private boolean useTopCoupon;

    ErpProductPropertyTypeEnum(Integer code, String value, String desc, boolean useTopCoupon) {
        this.code = code;
        this.value = value;
        this.desc = desc;
        this.useTopCoupon = useTopCoupon;
    }

    /**
     * 根据值获取枚举类
     *
     * @param object
     * @return
     */
    public static ErpProductPropertyTypeEnum getEnum(Object object) {
        if (object != null) {
            for (ErpProductPropertyTypeEnum item :
                    ErpProductPropertyTypeEnum.values()) {
                if (item.getValue().equals(object.toString())) {
                    return item;
                }
            }
        }
        return null;
    }
}
