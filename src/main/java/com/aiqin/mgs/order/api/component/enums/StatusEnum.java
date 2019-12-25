package com.aiqin.mgs.order.api.component.enums;

import com.aiqin.mgs.order.api.domain.SelectOptionItem;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * 二进制判断枚举类
 * TODO 特别注意：0表示肯定(是) 1表示否定（否）
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/12/17 19:41
 */
@Getter
public enum StatusEnum {

    /***逻辑-是*/
    NO(1, "1", "否", "未支付"),
    /***逻辑-否*/
    YES(0, "0", "是", "已支付");

    /***数字类型状态*/
    private Integer code;
    /***字符串类型状态*/
    private String value;
    /***状态描述*/
    private String desc;
    /***二进制支付成功状态描述*/
    private String paymentDesc;

    StatusEnum(Integer code, String value, String desc, String paymentDesc) {
        this.code = code;
        this.value = value;
        this.desc = desc;
        this.paymentDesc = paymentDesc;
    }

    /**
     * 获取二进制支付状态选项列表
     *
     * @return
     */
    public static List<SelectOptionItem> getPaymentSelectOptionList() {
        List<SelectOptionItem> list = new ArrayList<>();
        for (StatusEnum item :
                StatusEnum.values()) {
            list.add(new SelectOptionItem(item.getValue(), item.getPaymentDesc()));
        }
        return list;
    }

}
