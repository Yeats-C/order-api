package com.aiqin.mgs.order.api.component.returnenums;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
@ApiModel("退货--退货类型")
public enum ReturnOrderTypeEnum {

    //退货类型  0客户退货、1缺货退货、2售后退货、3冲减单

    CONSUMER_TYPE(0,"0客户退货"),
    OUT_OF_STOCK_TYPE(1,"缺货退货"),
    AFTER_SALE_TYPE(2,"售后退货"),
    WRITE_DOWN_ORDER_TYPE(3,"冲减单");

    private Integer code;

    private String name;

}
