package com.aiqin.mgs.order.api.component.enums;

import lombok.Getter;

/**
 * 发票类型枚举类
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/12/18 20:24
 */
@Getter
public enum ErpInvoiceTypeEnum {

    /***不开发票*/
    NO_INVOICE(1, "1", "不开发票"),
    /***普通增值税发票*/
    ORDINARY_VAT(2, "2", "普通增值税发票"),
    /***专用增值税发票*/
    SPECIAL_VAT(3, "3", "专用增值税发票");

    /***数字类型状态*/
    private Integer code;
    /***字符串类型状态*/
    private String value;
    /***状态描述*/
    private String desc;

    ErpInvoiceTypeEnum(Integer code, String value, String desc) {
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
    public static ErpInvoiceTypeEnum getEnum(Object object) {
        if (object != null) {
            for (ErpInvoiceTypeEnum item :
                    ErpInvoiceTypeEnum.values()) {
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
            ErpInvoiceTypeEnum anEnum = getEnum(object.toString());
            if (anEnum != null) {
                return anEnum.getDesc();
            }
        }
        return "";
    }
}
