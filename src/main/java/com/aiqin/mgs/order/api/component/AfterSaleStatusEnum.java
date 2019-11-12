package com.aiqin.mgs.order.api.component;

import lombok.Getter;

/**
 * 退货状态枚举类
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/11/11 15:12
 */
@Getter
public enum AfterSaleStatusEnum {

    /***审批中*/
    AFTER_SALE_STATUS_0(0, "0", "审批中"),
    /***处理中*/
    AFTER_SALE_STATUS_1(1, "1", "处理中"),
    /***退款中*/
    AFTER_SALE_STATUS_2(2, "2", "退款中"),
    /***已完成*/
    AFTER_SALE_STATUS_3(3, "3", "已完成"),
    /***已取消*/
    AFTER_SALE_STATUS_4(4, "4", "已取消"),
    /***已关闭*/
    AFTER_SALE_STATUS_5(5, "5", "已关闭");

    private Integer code;
    private String value;
    private String desc;

    AfterSaleStatusEnum(Integer code, String value, String desc) {
        this.code = code;
        this.value = value;
        this.desc = desc;
    }

    public static AfterSaleStatusEnum getEnum(Integer code) {
        for (AfterSaleStatusEnum type : AfterSaleStatusEnum.values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return null;
    }

    public static AfterSaleStatusEnum getEnum(String value) {
        for (AfterSaleStatusEnum type : AfterSaleStatusEnum.values()) {
            if (type.getValue().equals(value)) {
                return type;
            }
        }
        return null;
    }


    public static String getEnumDesc(Integer code) {
        for (AfterSaleStatusEnum type : AfterSaleStatusEnum.values()) {
            if (type.getCode().equals(code)) {
                return type.getDesc();
            }
        }
        return "";
    }

    public static String getEnumDesc(String value) {
        for (AfterSaleStatusEnum type : AfterSaleStatusEnum.values()) {
            if (type.getValue().equals(value)) {
                return type.getDesc();
            }
        }
        return "";
    }

}
