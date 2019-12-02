package com.aiqin.mgs.order.api.component;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
@ApiModel("业务场景")
public enum ServiceSceneEnum {
    TO_C(0,"TO C"),
    TO_B(1,"TO B");

    private Integer code;

    private String desc;

}
