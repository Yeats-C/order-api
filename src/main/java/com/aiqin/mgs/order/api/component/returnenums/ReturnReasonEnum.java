package com.aiqin.mgs.order.api.component.returnenums;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
@ApiModel("退货--退货原因")
public enum ReturnReasonEnum {

    ORDER_TYPE_ZS("14","质量退货"),
    ORDER_TYPE_PS("15","一般退货（无理由退货）"),
    ORDER_TYPE_HJ("16","物流破损");

    private String code;

    private String name;

}
