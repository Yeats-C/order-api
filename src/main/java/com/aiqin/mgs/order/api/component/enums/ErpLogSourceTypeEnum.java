package com.aiqin.mgs.order.api.component.enums;

import lombok.Getter;

/**
 * 日志来源类型
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/12/19 20:35
 */
@Getter
public enum ErpLogSourceTypeEnum {

    /***销售*/
    SALES(0, "0", "销售"),
    /***采购*/
    PURCHASE(1, "1", "采购"),
    /***退货*/
    RETURN(2, "2", "退货"),
    /***退供*/
    RETURN_SUPPLY(3, "3", "退供"),
    ;

    private Integer code;
    private String value;
    private String desc;

    ErpLogSourceTypeEnum(Integer code, String value, String desc) {
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
    public static ErpLogSourceTypeEnum getEnum(Object object) {
        if (object != null) {
            for (ErpLogSourceTypeEnum item :
                    ErpLogSourceTypeEnum.values()) {
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
            ErpLogSourceTypeEnum anEnum = getEnum(object.toString());
            if (anEnum != null) {
                return anEnum.getDesc();
            }
        }
        return "";
    }

}
