package com.aiqin.mgs.order.api.component.enums.pay;

import lombok.Getter;

/**
 * 支付单费用类型
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/12/9 15:07
 */
@Getter
public enum ErpPayFeeTypeEnum {

    /***订单费用*/
    ORDER_FEE(1, "1", "订单费用"),
    /***物流单费用*/
    LOGISTICS_FEE(2, "2", "物流费用");

    /***数字类型状态*/
    private Integer code;
    /***字符串类型状态*/
    private String value;
    /***状态描述*/
    private String desc;

    ErpPayFeeTypeEnum(Integer code, String value, String desc) {
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
    public static ErpPayFeeTypeEnum getEnum(Object object) {
        if (object != null) {
            for (ErpPayFeeTypeEnum item :
                    ErpPayFeeTypeEnum.values()) {
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
            ErpPayFeeTypeEnum anEnum = getEnum(object.toString());
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
            ErpPayFeeTypeEnum anEnum = getEnum(object.toString());
            if (anEnum != null) {
                return true;
            }
        }
        return false;
    }
}
