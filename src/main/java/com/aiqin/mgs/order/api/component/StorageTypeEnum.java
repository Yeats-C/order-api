package com.aiqin.mgs.order.api.component;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
@ApiModel("是否为赠品")
public enum StorageTypeEnum {
    DOOR_STORE(1,"门店自有仓"),
    CENTER_STORE(2,"代表配送中心大仓");

    private Integer code;

    private String desc;

}
