package com.aiqin.mgs.order.api.component.returnenums;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
@ApiModel("退货--订单类型")
public enum OrderTypeEnum {

    //订单类型 1直送 2配送 3货架
    ORDER_TYPE_ZS(1,"直送"),
    ORDER_TYPE_PS(2,"配送"),
    ORDER_TYPE_HJ(3,"货架");

    private Integer code;

    private String name;

}
