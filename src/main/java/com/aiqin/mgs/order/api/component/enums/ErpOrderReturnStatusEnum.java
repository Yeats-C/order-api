package com.aiqin.mgs.order.api.component.enums;

import lombok.Getter;

/**
 * 退货状态枚举类
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2020/1/14 16:28
 */
@Getter
public enum ErpOrderReturnStatusEnum {

    /***未发起退货*/
    NONE(1, "1", "未发起退货"),
    /***部分退货完成*/
    WAIT(2, "2", "部分退货"),
    /***全部退货完成*/
    SUCCESS(3, "3", "全部退货完成");

    private Integer code;
    private String value;
    private String desc;

    ErpOrderReturnStatusEnum(Integer code, String value, String desc) {
        this.code = code;
        this.value = value;
        this.desc = desc;
    }
}
