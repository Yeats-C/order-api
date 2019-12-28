package com.aiqin.mgs.order.api.component.enums.pay;

import lombok.Getter;

/**
 * 发起支付操作方式
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/12/27 10:19
 */
@Getter
public enum ErpRequestPayOperationTypeEnum {

    /***充值*/
    TYPE_1(1, "1", "充值"),
    /***消费*/
    TYPE_2(2, "2", "消费"),
    /***转账*/
    TYPE_3(3, "3", "转账"),
    /***退款*/
    TYPE_4(4, "4", "退款"),
    ;

    private Integer code;
    private String value;
    private String desc;

    ErpRequestPayOperationTypeEnum(Integer code, String value, String desc) {
        this.code = code;
        this.value = value;
        this.desc = desc;
    }

}
