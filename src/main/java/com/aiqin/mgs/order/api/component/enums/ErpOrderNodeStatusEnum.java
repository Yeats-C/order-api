package com.aiqin.mgs.order.api.component.enums;

import lombok.Getter;

@Getter
public enum ErpOrderNodeStatusEnum {

    /***商品*/
    STATUS_1(1, "1", "待支付", ErpOrderStatusEnum.ORDER_STATUS_1),
    STATUS_2(2, "2", "已发起支付", ErpOrderStatusEnum.ORDER_STATUS_1),
    STATUS_3(3, "3", "支付成功", ErpOrderStatusEnum.ORDER_STATUS_2),
    STATUS_4(4, "4", "支付失败", ErpOrderStatusEnum.ORDER_STATUS_1),
    STATUS_5(5, "5", "获取物流券成功", ErpOrderStatusEnum.ORDER_STATUS_3),
    STATUS_6(6, "6", "拆单成功", ErpOrderStatusEnum.ORDER_STATUS_4),
//    STATUS_7(7, "7", "同步到供应链成功", ErpOrderStatusEnum.ORDER_STATUS_6),
    STATUS_8(8, "8", "等待拣货", ErpOrderStatusEnum.ORDER_STATUS_6),
    STATUS_9(9, "9", "已发货", ErpOrderStatusEnum.ORDER_STATUS_11),
    STATUS_10(10, "10", "已支付物流费用", ErpOrderStatusEnum.ORDER_STATUS_12),
    ;

    private Integer code;
    private String value;
    private String desc;
    private ErpOrderStatusEnum orderStatusEnum;

    ErpOrderNodeStatusEnum(Integer code, String value, String desc, ErpOrderStatusEnum orderStatusEnum) {
        this.code = code;
        this.value = value;
        this.desc = desc;
        this.orderStatusEnum = orderStatusEnum;
    }

    /**
     * 根据值获取枚举类
     *
     * @param object
     * @return
     */
    public static ErpOrderNodeStatusEnum getEnum(Object object) {
        if (object != null) {
            for (ErpOrderNodeStatusEnum item :
                    ErpOrderNodeStatusEnum.values()) {
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
            ErpOrderNodeStatusEnum anEnum = getEnum(object.toString());
            if (anEnum != null) {
                return anEnum.getDesc();
            }
        }
        return "";
    }

}
