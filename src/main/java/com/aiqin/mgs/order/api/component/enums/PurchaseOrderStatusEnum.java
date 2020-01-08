package com.aiqin.mgs.order.api.component.enums;

import lombok.Data;
import lombok.Getter;

@Getter
public enum PurchaseOrderStatusEnum {
    //采购单状态
    PURCHASE_ORDER_STATUS__WAIT(0,"0","待审核"),
    PURCHASE_ORDER_STATUS_(1,"1","审核中"),
    PURCHASE_ORDER_STATUS_COM(2,"2","审核通过"),
    PURCHASE_ORDER_STATUS_SC(3,"3","备货确认"),
    PURCHASE_ORDER_STATUS_DC(4,"4","发货确认"),
    PURCHASE_ORDER_STATUS_WS(5,"5","入库开始"),
    PURCHASE_ORDER_STATUS_WARING(6,"6","入库中"),
    PURCHASE_ORDER_STATUS_WARED(7,"7","已入库"),
    PURCHASE_ORDER_STATUS_COMPLETE(8,"8","完成"),
    PURCHASE_ORDER_STATUS_REMOVE(9,"9","取消"),
    PURCHASE_ORDER_STATUS_FALL(10,"10","审核不通过");
    /***数字类型状态*/
    private Integer code;
    /***字符串类型状态*/
    private String value;
    /***状态描述*/
    private String desc;

    PurchaseOrderStatusEnum(Integer code, String value, String desc) {
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
