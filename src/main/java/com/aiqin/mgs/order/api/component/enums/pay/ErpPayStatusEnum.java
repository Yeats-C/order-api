package com.aiqin.mgs.order.api.component.enums.pay;

import com.aiqin.mgs.order.api.domain.SelectOptionItem;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * 支付状态枚举类
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/11/12 16:58
 */
@Getter
public enum ErpPayStatusEnum {

    /***待支付*/
    UNPAID(0, "0", "待支付","待退款"),
    /***已发起支付*/
    PAYING(1, "1", "支付中","退款中"),
    /***支付成功*/
    SUCCESS(2, "2", "支付成功","退款成功"),
    /***支付失败*/
    FAIL(3, "3", "支付失败","退款失败");

    private Integer code;
    private String value;
    private String desc;
    private String refundDesc;

    ErpPayStatusEnum(Integer code, String value, String desc, String refundDesc) {
        this.code = code;
        this.value = value;
        this.desc = desc;
        this.refundDesc = refundDesc;
    }

    public static ErpPayStatusEnum getEnum(Object object) {
        if (object != null) {
            for (ErpPayStatusEnum item :
                    ErpPayStatusEnum.values()) {
                if (item.getValue().equals(object.toString())) {
                    return item;
                }
            }
        }
        return null;
    }

    public static String getEnumDesc(Object object) {
        if (object != null) {
            ErpPayStatusEnum anEnum = getEnum(object.toString());
            if (anEnum != null) {
                return anEnum.getDesc();
            }
        }
        return "";
    }

    public static String getRefundDesc(Object object) {
        if (object != null) {
            ErpPayStatusEnum anEnum = getEnum(object.toString());
            if (anEnum != null) {
                return anEnum.getRefundDesc();
            }
        }
        return "";
    }

    public static List<SelectOptionItem> getSelectOptionList() {
        List<SelectOptionItem> list = new ArrayList<>();
        for (ErpPayStatusEnum item :
                ErpPayStatusEnum.values()) {
            list.add(new SelectOptionItem(item.getValue(), item.getDesc()));
        }
        return list;
    }
}
