package com.aiqin.mgs.order.api.component.enums;

import com.aiqin.mgs.order.api.domain.EnumItemInfo;
import lombok.Getter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 支付单费用类型
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/12/9 15:07
 */
@Getter
public enum ErpPayFeeTypeEnum {

    /***订单费用*/
    ORDER_FEE(1, "1", "订单费用"),
    /***物流单费用*/
    LOGISTICS_FEE(2, "2", "物流费用");

    /***数字类型状态*/
    private Integer code;
    /***字符串类型状态*/
    private String value;
    /***状态描述*/
    private String desc;

    ErpPayFeeTypeEnum(Integer code, String value, String desc) {
        this.code = code;
        this.value = value;
        this.desc = desc;
    }
    /***选项类型*/
    public static final List<EnumItemInfo> SELECT_LIST = new ArrayList<>();
    /***code-enum map*/
    public static final Map<Integer, ErpPayFeeTypeEnum> CODE_ENUM_MAP = new LinkedHashMap<>(16);
    /***value-enum map*/
    public static final Map<String, ErpPayFeeTypeEnum> VALUE_ENUM_MAP = new LinkedHashMap<>(16);

    static {
        for (ErpPayFeeTypeEnum item :
                ErpPayFeeTypeEnum.values()) {
            SELECT_LIST.add(new EnumItemInfo(item.getCode(), item.getValue(), item.getDesc()));
            CODE_ENUM_MAP.put(item.getCode(), item);
            VALUE_ENUM_MAP.put(item.getValue(), item);
        }
    }

    public static ErpPayFeeTypeEnum getEnum(Object object) {
        if (object != null) {
            return VALUE_ENUM_MAP.get(object.toString());
        }
        return null;
    }

    public static String getEnumDesc(Object object) {
        if (object != null) {
            ErpPayFeeTypeEnum anEnum = VALUE_ENUM_MAP.get(object.toString());
            if (anEnum != null) {
                return anEnum.getDesc();
            }
        }
        return "";
    }

    public static boolean exist(Object object) {
        if (object != null) {
            return VALUE_ENUM_MAP.containsKey(object.toString());
        }
        return false;
    }
}
