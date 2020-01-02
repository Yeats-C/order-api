package com.aiqin.mgs.order.api.component.returnenums;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
@ApiModel("退货--处理方法")
public enum TreatmentMethodEnum {

    //处理办法 1--退货退款(通过) 2--挂账 3--不通过(驳回) 4--仅退款 99--已取消

    RETURN_AMOUNT_AND_GOODS_TYPE(1,"退货退款"),
    BOOK_TYPE(2,"挂账"),
    FALL_TYPE(3,"不通过(驳回)"),
    RETURN_AMOUNT_TYPE(4,"仅退款"),
    CANCEL_TYPE(99,"已取消");

    private Integer code;

    private String name;

}
