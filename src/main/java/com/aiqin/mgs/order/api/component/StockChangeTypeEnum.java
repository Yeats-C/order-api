package com.aiqin.mgs.order.api.component;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
@ApiModel("库存变更类型")
public enum StockChangeTypeEnum {
    IN_STORAGE(0,"入库"),
    OUT_STORAGE(1,"出库"),
    PACKAGE_OUT(2,"组合套餐包出库");

    private Integer code;

    private String desc;

}
