package com.aiqin.mgs.order.api.component;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
@ApiModel("是否为赠品")
public enum BillTypeEnum {
    INITIALIZE(1,"初始化入库"),
    DOOR_RETURN(2,"门店销售退货"),
    ONLINE_RETURN(3,"网店销售退货"),
    DOOR_SALE(4,"门店销售"),
    ONLINE_SALE(5,"网店销售"),
    PACKAGE_SALE(22,"组合套餐包出库");

    private Integer code;

    private String desc;

}
