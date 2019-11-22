package com.aiqin.mgs.order.api.component.enums;

import lombok.Getter;

/**
 * 订单来源
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/11/19 10:44
 */
@Getter
public enum OrderOriginTypeEnum {

    /***POS*/
    ORIGIN_COME_3(3, "3", "POS"),
    /***微商城*/
    ORIGIN_COME_4(4, "4", "微商城"),
    /***WEB*/
    ORIGIN_COME_5(5, "5", "WEB");

    private Integer code;
    private String value;
    private String desc;

    OrderOriginTypeEnum(Integer code, String value, String desc) {
        this.code = code;
        this.value = value;
        this.desc = desc;
    }

    public static boolean exist(Integer code) {
        for (OrderOriginTypeEnum type : OrderOriginTypeEnum.values()) {
            if (type.getCode().equals(code)) {
                return true;
            }
        }
        return false;
    }

    public static boolean exist(String value) {
        for (OrderOriginTypeEnum type : OrderOriginTypeEnum.values()) {
            if (type.getValue().equals(value)) {
                return true;
            }
        }
        return false;
    }

}
