package com.aiqin.mgs.order.api.component.enums;

import lombok.Getter;

/**
 * 订单生成冲减单状态枚举类
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2020/1/6 17:35
 */
@Getter
public enum ErpOrderScourSheetStatusEnum {

    /***不需要生成冲减单*/
    NOT_NEED(1, "1", "不需要生成冲减单"),
    /***等待生成冲减单*/
    WAIT(2, "2", "等待生成冲减单"),
    /***生成冲减单完成*/
    SUCCESS(3, "3", "生成冲减单完成");

    private Integer code;
    private String value;
    private String desc;

    ErpOrderScourSheetStatusEnum(Integer code, String value, String desc) {
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
    public static ErpOrderScourSheetStatusEnum getEnum(Object object) {
        if (object != null) {
            for (ErpOrderScourSheetStatusEnum item :
                    ErpOrderScourSheetStatusEnum.values()) {
                if (item.getValue().equals(object.toString())) {
                    return item;
                }
            }
        }
        return null;
    }
}
