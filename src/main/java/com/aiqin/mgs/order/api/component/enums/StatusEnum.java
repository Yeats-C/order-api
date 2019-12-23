package com.aiqin.mgs.order.api.component.enums;

import lombok.Getter;

/**
 * 二进制判断枚举类
 * TODO 特别注意：0表示肯定(是) 1表示否定（否）
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/12/17 19:41
 */
@Getter
public enum StatusEnum {

    /***逻辑-是*/
    NO(1, "1", "否"),
    /***逻辑-否*/
    YES(0, "0", "是");

    /***数字类型状态*/
    private Integer code;
    /***字符串类型状态*/
    private String value;
    /***状态描述*/
    private String desc;

    StatusEnum(Integer code, String value, String desc) {
        this.code = code;
        this.value = value;
        this.desc = desc;
    }
}
