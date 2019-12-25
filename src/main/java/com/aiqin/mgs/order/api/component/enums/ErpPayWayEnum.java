package com.aiqin.mgs.order.api.component.enums;

import com.aiqin.mgs.order.api.domain.SelectOptionItem;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * 支付方式枚举
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/11/12 17:15
 */
@Getter
public enum ErpPayWayEnum {

    /***余额*/
    PAY_1(1, "1", "余额支付");

    private Integer code;
    private String value;
    private String desc;

    ErpPayWayEnum(Integer code, String value, String desc) {
        this.code = code;
        this.value = value;
        this.desc = desc;
    }

    /**
     * 根据值获取枚举类
     *
     * @param object
     * @return
     */
    public static ErpPayWayEnum getEnum(Object object) {
        if (object != null) {
            for (ErpPayWayEnum item :
                    ErpPayWayEnum.values()) {
                if (item.getValue().equals(object.toString())) {
                    return item;
                }
            }
        }
        return null;
    }

    /**
     * 根据枚举类的值获取枚举的描述
     *
     * @param object
     * @return
     */
    public static String getEnumDesc(Object object) {
        if (object != null) {
            ErpPayWayEnum anEnum = getEnum(object.toString());
            if (anEnum != null) {
                return anEnum.getDesc();
            }
        }
        return "";
    }


    /**
     * 校验值是否存在
     *
     * @param object
     * @return
     */
    public static boolean exist(Object object) {
        if (object != null) {
            ErpPayWayEnum anEnum = getEnum(object.toString());
            if (anEnum != null) {
                return true;
            }
        }
        return false;
    }

    public static List<SelectOptionItem> getSelectOptionList() {
        List<SelectOptionItem> list = new ArrayList<>();
        for (ErpPayWayEnum item :
                ErpPayWayEnum.values()) {
            list.add(new SelectOptionItem(item.getValue(), item.getDesc()));
        }
        return list;
    }

}
