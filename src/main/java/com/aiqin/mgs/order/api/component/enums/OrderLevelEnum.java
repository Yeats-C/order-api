package com.aiqin.mgs.order.api.component.enums;

import lombok.Getter;

/**
 * 订单附属级别
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/11/15 15:42
 */
@Getter
public enum OrderLevelEnum {

    /***主订单*/
    PRIMARY(1, "1", "主订单"),
    /***子订单*/
    SECONDARY(2, "2", "子订单");

    /***数字类型状态*/
    private Integer code;
    /***字符串类型状态*/
    private String value;
    /***状态描述*/
    private String desc;

    OrderLevelEnum(Integer code, String value, String desc) {
        this.code = code;
        this.value = value;
        this.desc = desc;
    }

}
