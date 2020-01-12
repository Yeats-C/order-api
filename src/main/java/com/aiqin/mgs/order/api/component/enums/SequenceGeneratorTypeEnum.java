package com.aiqin.mgs.order.api.component.enums;

import lombok.Getter;

/**
 * 模拟序列类型
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2020/1/11 14:27
 */
@Getter
public enum SequenceGeneratorTypeEnum {

    /***订单号*/
    ORDER_STORE_CODE("order_store_code", "订单号"),
    ;

    /***字符串类型状态*/
    private String value;
    /***状态描述*/
    private String desc;

    SequenceGeneratorTypeEnum(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

}
