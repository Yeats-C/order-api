package com.aiqin.mgs.order.api.component;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
@ApiModel("操作类型")
public enum OperationTypeEnum {
    CONSUME(2,"消费"),
    RECHARGE(1,"充值");

    private Integer code;

    private String desc;

}
