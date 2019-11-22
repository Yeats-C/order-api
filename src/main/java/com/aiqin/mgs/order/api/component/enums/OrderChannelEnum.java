package com.aiqin.mgs.order.api.component.enums;

import lombok.Getter;

/**
 * 订单销售渠道标识
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/11/19 10:02
 */
@Getter
public enum OrderChannelEnum {

    /***总部向门店销售*/
    CHANNEL_3(3, "3", "总部向门店销售"),
    /***门店向会员销售*/
    CHANNEL_4(4, "4", "门店向会员销售");

    private Integer code;
    private String value;
    private String desc;

    OrderChannelEnum(Integer code, String value, String desc) {
        this.code = code;
        this.value = value;
        this.desc = desc;
    }

    public static boolean exist(Integer code) {
        for (OrderChannelEnum type : OrderChannelEnum.values()) {
            if (type.getCode().equals(code)) {
                return true;
            }
        }
        return false;
    }

    public static boolean exist(String value) {
        for (OrderChannelEnum type : OrderChannelEnum.values()) {
            if (type.getValue().equals(value)) {
                return true;
            }
        }
        return false;
    }

}
