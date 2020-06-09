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

    /***正常补货*/
    ORDER_TYPE_1(1, "1", "正常补货",false),
    /***普通首单*/
    ORDER_TYPE_2(2, "2", "普通首单",true),
    /***首单赠送*/
    ORDER_TYPE_4(4, "4", "首单赠送",true),
    /***新店货架*/
    ORDER_TYPE_16(16, "16", "新店货架",false),
    /***货架补货*/
    ORDER_TYPE_17(17, "17", "货架补货",false),
    /***游泳游乐*/
    ORDER_TYPE_172(172, "172", "游泳游乐",false),
    /***批发订单*/
    ORDER_TYPE_54(51, "51", "批发订单",false);

    /***数字编码*/
    private Integer code;
    /***字符串编码*/
    private String value;
    /***描述*/
    private String desc;
    /***是否是首单*/
    private boolean firstOrder;

    ErpOrderCategoryEnum(Integer code, String value, String desc, boolean firstOrder) {
        this.code = code;
        this.value = value;
        this.desc = desc;
        this.firstOrder = firstOrder;
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

