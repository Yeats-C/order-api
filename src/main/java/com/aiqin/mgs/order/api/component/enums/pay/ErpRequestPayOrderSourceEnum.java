package com.aiqin.mgs.order.api.component.enums.pay;

import lombok.Getter;

/**
 * 发起支付参数订单来源枚举类
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/12/27 10:12
 */
@Getter
public enum ErpRequestPayOrderSourceEnum {

    /***POS*/
    POS(0, "0", "POS"),
    /***微商城*/
    MICRO_MALL(1, "1", "微商城"),
    /***全部*/
    ALL(2, "2", "全部"),
    /***WEB*/
    WEB(3, "3", "WEB"),
    ;

    private Integer code;
    private String value;
    private String desc;

    ErpRequestPayOrderSourceEnum(Integer code, String value, String desc) {
        this.code = code;
        this.value = value;
        this.desc = desc;
    }

}
