package com.aiqin.mgs.order.api.component.enums;

import com.aiqin.mgs.order.api.domain.EnumItemInfo;
import lombok.Getter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 订单附属级别
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/11/15 15:42
 */
@Getter
public enum ErpOrderLevelEnum {

    /***主订单*/
    PRIMARY(0, "0", "主订单"),
    /***子订单*/
    SECONDARY(1, "1", "子订单");

    /***数字类型状态*/
    private Integer code;
    /***字符串类型状态*/
    private String value;
    /***状态描述*/
    private String desc;

    ErpOrderLevelEnum(Integer code, String value, String desc) {
        this.code = code;
        this.value = value;
        this.desc = desc;
    }
    /***选项类型*/
    public static final List<EnumItemInfo> SELECT_LIST = new ArrayList<>();
    /***value-enum map*/
    public static final Map<String, ErpOrderLevelEnum> VALUE_ENUM_MAP = new LinkedHashMap<>(16);

    static {
        for (ErpOrderLevelEnum item :
                ErpOrderLevelEnum.values()) {
            SELECT_LIST.add(new EnumItemInfo(item.getCode(), item.getValue(), item.getDesc()));
            VALUE_ENUM_MAP.put(item.getValue(), item);
        }
    }

    public static ErpOrderLevelEnum getEnum(Object object) {
        if (object != null) {
            return VALUE_ENUM_MAP.get(object.toString());
        }
        return null;
    }

    public static String getEnumDesc(Object object) {
        if (object != null) {
            ErpOrderLevelEnum anEnum = VALUE_ENUM_MAP.get(object.toString());
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
