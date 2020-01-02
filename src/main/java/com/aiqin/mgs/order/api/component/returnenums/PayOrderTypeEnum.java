package com.aiqin.mgs.order.api.component.returnenums;

/**
 * 支付中心--订单类型 14配送tob 2直送tob
 * 
 * @author hantao
 *
 */
public enum PayOrderTypeEnum {

    PAY_ORDER_TYPE_ZHI(14, "配送tob"),
    PAY_ORDER_TYPE_PEI(2, "直送tob");

    private Integer key;
    private String msg;

    PayOrderTypeEnum(Integer key, String msg) {
        this.key = key;
        this.msg = msg;
    }

    public Integer getKey() {
        return key;
    }

    public String getMsg() {
        return msg;
    }

}
