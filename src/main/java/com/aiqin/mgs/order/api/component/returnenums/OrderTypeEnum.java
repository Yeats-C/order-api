package com.aiqin.mgs.order.api.component.returnenums;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
@ApiModel("退货--订单类型")
public enum OrderTypeEnum {

    //订单类型 1配送 2直送 3货架直送 4采购直送
//    ORDER_TYPE_ZS(1,"直送"),
//    ORDER_TYPE_PS(2,"配送"),
//    ORDER_TYPE_HJ(3,"货架");
    ORDER_TYPE_PS(1,"配送"),
    ORDER_TYPE_ZS(2,"直送"),
    ORDER_TYPE_HJ(3,"货架"),
    ORDER_TYPE_CG(4,"采购");

    private Integer code;

    private String name;

}
