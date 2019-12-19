package com.aiqin.mgs.order.api.component.enums;

import com.aiqin.mgs.order.api.domain.EnumItemInfo;
import lombok.Getter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 订单类型枚举
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/11/15 11:09
 */
@Getter
public enum ErpOrderTypeEnum {

    /***直送*/
    DIRECT_SEND(0, "0", "直送"),
    /***配送*/
    DISTRIBUTION(1, "1", "配送"),
    /***辅采直送*/
    ASSIST_PURCHASING(2, "2", "辅采直送");

    private Integer code;
    private String value;
    private String desc;

    ErpOrderTypeEnum(Integer code, String value, String desc) {
        this.code = code;
        this.value = value;
        this.desc = desc;
    }

    /***选项类型*/
    public static final List<EnumItemInfo> SELECT_LIST = new ArrayList<>();
    /***value-enum map*/
    public static final Map<String, ErpOrderTypeEnum> VALUE_ENUM_MAP = new LinkedHashMap<>(16);

    static {
        for (ErpOrderTypeEnum item :
                ErpOrderTypeEnum.values()) {
            SELECT_LIST.add(new EnumItemInfo(item.getCode(), item.getValue(), item.getDesc()));
            VALUE_ENUM_MAP.put(item.getValue(), item);
        }
    }

    public static ErpOrderTypeEnum getEnum(Object object) {
        if (object != null) {
            return VALUE_ENUM_MAP.get(object.toString());
        }
        return null;
    }

    public static String getEnumDesc(Object object) {
        if (object != null) {
            ErpOrderTypeEnum anEnum = VALUE_ENUM_MAP.get(object.toString());
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
