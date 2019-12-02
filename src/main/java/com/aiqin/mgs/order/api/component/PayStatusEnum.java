package com.aiqin.mgs.order.api.component;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
@ApiModel("支付类型")
public enum PayStatusEnum {
    NO_PAY(0,"未支付"),
    HAS_PAY(1,"支付完成");

    private Integer code;

    private String desc;

}
