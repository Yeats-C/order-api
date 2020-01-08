package com.aiqin.mgs.order.api.component.enums;

import lombok.Getter;

/**
 * 订单类别枚举
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/11/12 17:00
 */
@Getter
public enum ErpOrderCategoryEnum {

    /***首单配送*/
    ORDER_TYPE_1(1, "1", "正常补货"),
    /***首单赠送*/
    ORDER_TYPE_2(2, "2", "普通首单"),
    /***配送补货*/
    ORDER_TYPE_4(4, "4", "首单赠送"),
    /***首单直送*/
    ORDER_TYPE_16(16, "16", "新店货架"),
    /***直送补货*/
    ORDER_TYPE_17(17, "17", "货架补货"),
    /***首单货架*/
    ORDER_TYPE_172(172, "172", "游泳游乐");

    /***数字编码*/
    private Integer code;
    /***字符串编码*/
    private String value;
    /***描述*/
    private String desc;

    ErpOrderCategoryEnum(Integer code, String value, String desc) {
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
    public static ErpOrderCategoryEnum getEnum(Object object) {
        if (object != null) {
            for (ErpOrderCategoryEnum item :
                    ErpOrderCategoryEnum.values()) {
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
            ErpOrderCategoryEnum anEnum = getEnum(object.toString());
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
            ErpOrderCategoryEnum anEnum = getEnum(object.toString());
            if (anEnum != null) {
                return true;
            }
        }
        return false;
    }

}

