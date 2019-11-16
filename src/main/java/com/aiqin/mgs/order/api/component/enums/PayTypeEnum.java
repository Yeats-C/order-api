package com.aiqin.mgs.order.api.component.enums;

import lombok.Getter;

/**
 * 支付类型枚举
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/11/12 17:15
 */
@Getter
public enum PayTypeEnum {

    /***在线支付-微信*/
    ONLINE_WE_CHAT(0, "0", "在线支付-微信"),
    /***在线支付-支付宝*/
    ONLINE_ALI_PAY(1, "1", "在线支付-支付宝"),
    /***在线支付-银行卡*/
    ONLINE_BANK_CARD(2, "2", "在线支付-银行卡"),
    /***到店支付-现金*/
    CASH(3, "3", "到店支付-现金"),
    /***到店支付-微信*/
    DOOR_WE_CHAT(4, "4", "到店支付-微信"),
    /***到店支付-支付宝*/
    DOOR_ALI_PAY(5, "5", "到店支付-支付宝"),
    /***到店支付-银行卡*/
    DOOR_BANK_CARD(6, "6", "到店支付-银行卡"),
    /***储值卡支付*/
    BALANCE_PAY(7, "7", "储值卡支付");

    private Integer code;
    private String value;
    private String desc;

    PayTypeEnum(Integer code, String value, String desc) {
        this.code = code;
        this.value = value;
        this.desc = desc;
    }

    public static PayTypeEnum getEnum(Integer code) {
        for (PayTypeEnum type : PayTypeEnum.values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return null;
    }

    public static PayTypeEnum getEnum(String value) {
        for (PayTypeEnum type : PayTypeEnum.values()) {
            if (type.getValue().equals(value)) {
                return type;
            }
        }
        return null;
    }


    public static String getEnumDesc(Integer code) {
        for (PayTypeEnum type : PayTypeEnum.values()) {
            if (type.getCode().equals(code)) {
                return type.getDesc();
            }
        }
        return "";
    }

    public static String getEnumDesc(String value) {
        for (PayTypeEnum type : PayTypeEnum.values()) {
            if (type.getValue().equals(value)) {
                return type.getDesc();
            }
        }
        return "";
    }

}
