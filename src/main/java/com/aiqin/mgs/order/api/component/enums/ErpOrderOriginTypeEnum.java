package com.aiqin.mgs.order.api.component.enums;

import lombok.Getter;

/**
 * 订单来源
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/11/19 10:44
 */
@Getter
public enum ErpOrderOriginTypeEnum {

    /***POS*/
    ORIGIN_COME_0(101, "101", "POS"),
    /***微商城*/
    ORIGIN_COME_1(102, "102", "微商城"),
    /***WEB*/
    ORIGIN_COME_2(103, "103", "WEB");

    private Integer code;
    private String value;
    private String desc;

    ErpOrderOriginTypeEnum(Integer code, String value, String desc) {
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
    public static ErpOrderOriginTypeEnum getEnum(Object object) {
        if (object != null) {
            for (ErpOrderOriginTypeEnum item :
                    ErpOrderOriginTypeEnum.values()) {
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
            ErpOrderOriginTypeEnum anEnum = getEnum(object.toString());
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
            ErpOrderOriginTypeEnum anEnum = getEnum(object.toString());
            if (anEnum != null) {
                return true;
            }
        }
        return false;
    }

}
