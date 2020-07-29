package com.aiqin.mgs.order.api.component.returnenums;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
@ApiModel("退货--退货类型")
public enum ReturnOrderTypeEnum {

    //退货类型  0客户退货、1缺货退货、2售后退货、3冲减单 、4客户取消

    CONSUMER_TYPE(0,"0客户退货"),
    OUT_OF_STOCK_TYPE(1,"缺货退货"),
    AFTER_SALE_TYPE(2,"售后退货"),
    WRITE_DOWN_ORDER_TYPE(3,"冲减单"),
    CANCEL_ORDER(4,"客户取消"),
    QUALITY_ISSUES(6,"质量问题");

    private Integer code;

    private String name;

    public static ReturnOrderTypeEnum getEnum(Object object) {
        if (object != null) {
            for (ReturnOrderTypeEnum item :
                    ReturnOrderTypeEnum.values()) {
                if (item.getCode().equals(object.toString())) {
                    return item;
                }
            }
        }
        return null;
    }

    public static ReturnOrderTypeEnum getEnums(Integer code) {
        if (code != null) {
            for (ReturnOrderTypeEnum item :
                    ReturnOrderTypeEnum.values()) {
                if (item.getCode().equals(code)) {
                    return item;
                }
            }
        }
        return null;
    }
}
