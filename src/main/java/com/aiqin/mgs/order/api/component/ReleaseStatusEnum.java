package com.aiqin.mgs.order.api.component;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
@ApiModel("库存释放类型")
public enum ReleaseStatusEnum {
    NO_RELEASE(0,"未释放"),
    RELEASE(1,"释放");

    private Integer code;

    private String desc;

}
