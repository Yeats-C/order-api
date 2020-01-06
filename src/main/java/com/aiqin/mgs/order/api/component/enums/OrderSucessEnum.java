package com.aiqin.mgs.order.api.component.enums;

import lombok.Getter;

@Getter
public enum OrderSucessEnum {

    ORDER_SYNCHRO_NO(0,"0","不生成采购单"),
    ORDER_SYNCHRO_WAIT(1,"1","待生成采购单"),
    ORDER_SYNCHRO_SUCCESS(2,"2","采购单生成成功");


    /***数字类型状态*/
    private Integer code;
    /***字符串类型状态*/
    private String value;
    /***状态描述*/
    private String desc;

    OrderSucessEnum(Integer code, String value, String desc) {
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
