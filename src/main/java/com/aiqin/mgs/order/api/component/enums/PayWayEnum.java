package com.aiqin.mgs.order.api.component.enums;

import com.aiqin.mgs.order.api.domain.EnumItemInfo;
import lombok.Getter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 支付方式枚举
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/11/12 17:15
 */
@Getter
public enum PayWayEnum {

    /***余额*/
    PAY_1(1, "1", "余额支付");

    private Integer code;
    private String value;
    private String desc;

    PayWayEnum(Integer code, String value, String desc) {
        this.code = code;
        this.value = value;
        this.desc = desc;
    }

    /***选项类型*/
    public static List<EnumItemInfo> SELECT_LIST = new ArrayList<>();
    /***code-enum map*/
    public static Map<Integer, PayWayEnum> CODE_ENUM_MAP = new LinkedHashMap<>(16);
    /***value-enum map*/
    public static Map<String, PayWayEnum> VALUE_ENUM_MAP = new LinkedHashMap<>(16);

    static {
        for (PayWayEnum item :
                PayWayEnum.values()) {
            SELECT_LIST.add(new EnumItemInfo(item.getCode(), item.getValue(), item.getDesc()));
            CODE_ENUM_MAP.put(item.getCode(), item);
            VALUE_ENUM_MAP.put(item.getValue(), item);
        }
    }

    public static PayWayEnum getEnum(Object object) {
        if (object != null) {
            return VALUE_ENUM_MAP.get(object.toString());
        }
        return null;
    }

    public static String getEnumDesc(Object object) {
        if (object != null) {
            PayWayEnum anEnum = VALUE_ENUM_MAP.get(object.toString());
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
