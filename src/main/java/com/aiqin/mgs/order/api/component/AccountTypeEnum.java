package com.aiqin.mgs.order.api.component;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
@ApiModel("账户类型")
public enum AccountTypeEnum {
    ACCOUNTTYPE_0(0,"收款账户"),
    ACCOUNTTYPE_1(1,"平台账户"),
    ACCOUNTTYPE_2(2,"配送账户"),
    ACCOUNTTYPE_3(3,"直送账户"),
    ACCOUNTTYPE_4(4,"门店账户");
    private Integer code;

    private String desc;

}
