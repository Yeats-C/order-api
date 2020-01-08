package com.aiqin.mgs.order.api.component.returnenums;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
@ApiModel("退货--冲减单状态")
public enum WriteDownOrderStatusEnum {

    CREATE_ORDER_STATUS(0,"生成冲减单"),
    CREATE_REFUND_STATUS(1,"生成退款单"),
    REFUND_SUCCESS_STATUS(2,"退款成功"),
    CANCEL_ORDER(3,"客户取消订单");

    private Integer code;

    private String name;

}
