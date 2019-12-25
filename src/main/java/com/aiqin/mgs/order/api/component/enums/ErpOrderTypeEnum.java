package com.aiqin.mgs.order.api.component.enums;

import lombok.Getter;

/**
 * 订单类型枚举
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/11/15 11:09
 */
@Getter
public enum ErpOrderTypeEnum {

    /***直送*/
    DIRECT_SEND(0, "0", "直送"),
    /***配送*/
    DISTRIBUTION(1, "1", "配送"),
    /***辅采直送*/
    ASSIST_PURCHASING(2, "2", "辅采直送");

    private Integer code;
    private String value;
    private String desc;

    ErpOrderTypeEnum(Integer code, String value, String desc) {
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
    public static ErpOrderTypeEnum getEnum(Object object) {
        if (object != null) {
            for (ErpOrderTypeEnum item :
                    ErpOrderTypeEnum.values()) {
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
            ErpOrderTypeEnum anEnum = getEnum(object.toString());
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
            ErpOrderTypeEnum anEnum = getEnum(object.toString());
            if (anEnum != null) {
                return true;
            }
        }
        return false;
    }
}
