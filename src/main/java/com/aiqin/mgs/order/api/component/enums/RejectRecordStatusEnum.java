package com.aiqin.mgs.order.api.component.enums;

import lombok.Getter;

@Getter
public enum RejectRecordStatusEnum {
    //退供单状态
    PURCHASE_ORDER_STATUS_DC(0,"0","待确认"),
    PURCHASE_ORDER_STATUS_COMPLETE(1,"1","完成"),
    PURCHASE_ORDER_STATUS_REMOVE(2,"2","取消");
    /***数字类型状态*/
    private Integer code;
    /***字符串类型状态*/
    private String value;
    /***状态描述*/
    private String desc;

    RejectRecordStatusEnum(Integer code, String value, String desc) {
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
