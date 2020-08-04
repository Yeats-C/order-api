package com.aiqin.mgs.order.api.component.ReturnOrderEnum;

import com.aiqin.mgs.order.api.domain.response.ReturnOrderTypeResponse;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public enum ReturnOrderEnum {

    //     0客户退货、1缺货退货、2售后退货、3冲减单 、4客户取消  5赠品划单
    RETURN_ORDER_TYPE_0("0","客户退货"),
    RETURN_ORDER_TYPE_1("1","缺货退货"),
    RETURN_ORDER_TYPE_2("2","售后退货"),
    RETURN_ORDER_TYPE_3("3","冲减单"),
    RETURN_ORDER_TYPE_4("4","客户取消"),
    RETURN_ORDER_TYPE_5("5","赠品划单"),
    RETURN_ORDER_TYPE_6("6","质量问题退货");



    private String code;
    private String value;

    ReturnOrderEnum(String code, String value) {
        this.code = code;
        this.value = value;
    }

    public static List<ReturnOrderTypeResponse>  getEumValueList() {
         List<ReturnOrderTypeResponse> list = new ArrayList<>();
        for(ReturnOrderEnum sting:ReturnOrderEnum.values()) {
           ReturnOrderTypeResponse returnOrderTypeResponse = new ReturnOrderTypeResponse();
           returnOrderTypeResponse.setReturnOrderCode(sting.getCode());
           returnOrderTypeResponse.setReturnOrderName(sting.getValue());
           list.add(returnOrderTypeResponse);
        }
        return list;

    }
}
