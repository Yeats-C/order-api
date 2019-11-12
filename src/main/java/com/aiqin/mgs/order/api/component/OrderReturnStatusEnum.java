package com.aiqin.mgs.order.api.component;

import lombok.Getter;

/**
 * 退货状态枚举
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/11/11 16:49
 */
@Getter
public enum OrderReturnStatusEnum {

    /***待审核*/
    RETURN_STATUS_01(1, "1", "待审核"),
    /***审核通过*/
    RETURN_STATUS_02(2, "2", "审核通过"),
    /***退单同步中*/
    RETURN_STATUS_03(3, "3", "退单同步中"),
    /***等待退货验收*/
    RETURN_STATUS_04(4, "4", "等待退货验收"),
    /***等待退货入库*/
    RETURN_STATUS_05(5, "5", "等待退货入库"),
    /***等待审批*/
    RETURN_STATUS_06(6, "6", "等待审批"),
    /***退货完成*/
    RETURN_STATUS_11(11, "11", "退货完成"),
    /***退款完成*/
    RETURN_STATUS_12(12, "12", "退款完成"),
    /***退货异常终止*/
    RETURN_STATUS_97(97, "97", "退货异常终止"),
    /***审核不通过*/
    RETURN_STATUS_98(98, "98", "审核不通过"),
    /***已取消*/
    RETURN_STATUS_99(99, "99", "已取消");

    private Integer code;
    private String value;
    private String desc;

    OrderReturnStatusEnum(Integer code, String value, String desc) {
        this.code = code;
        this.value = value;
        this.desc = desc;
    }

    public static OrderReturnStatusEnum getEnum(Integer code) {
        for (OrderReturnStatusEnum type : OrderReturnStatusEnum.values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return null;
    }

    public static OrderReturnStatusEnum getEnum(String value) {
        for (OrderReturnStatusEnum type : OrderReturnStatusEnum.values()) {
            if (type.getValue().equals(value)) {
                return type;
            }
        }
        return null;
    }


    public static String getEnumDesc(Integer code) {
        for (OrderReturnStatusEnum type : OrderReturnStatusEnum.values()) {
            if (type.getCode().equals(code)) {
                return type.getDesc();
            }
        }
        return "";
    }

    public static String getEnumDesc(String value) {
        for (OrderReturnStatusEnum type : OrderReturnStatusEnum.values()) {
            if (type.getValue().equals(value)) {
                return type.getDesc();
            }
        }
        return "";
    }
}



