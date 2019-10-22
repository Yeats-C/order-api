package com.aiqin.mgs.order.api.component;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
@ApiModel("支付类型")
public enum PayTypeEnum {
    ONLINE_WE_CHAT(0,"在线支付-微信"),
    ONLINE_ALI_PAY(1,"在线支付-支付宝"),
    ONLINE_BANK_CARD(2,"在线支付-银行卡"),
    CASH(3,"到店支付-现金"),
    DOOR_WE_CHAT(4,"到店支付-微信"),
    DOOR_ALI_PAY(5,"到店支付-支付宝"),
    DOOR_BANK_CARD(6,"到店支付-银行卡"),
    BALANCE_PAY(7,"储值卡支付");
    private Integer code;

    private String desc;

}
