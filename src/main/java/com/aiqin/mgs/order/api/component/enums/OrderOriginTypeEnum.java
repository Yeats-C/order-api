package com.aiqin.mgs.order.api.component.enums;

import com.aiqin.mgs.order.api.domain.EnumItemInfo;
import lombok.Getter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 订单来源
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/11/19 10:44
 */
@Getter
public enum OrderOriginTypeEnum {

    /***POS*/
    ORIGIN_COME_3(3, "3", "POS"),
    /***微商城*/
    ORIGIN_COME_4(4, "4", "微商城"),
    /***WEB*/
    ORIGIN_COME_5(5, "5", "WEB");

    private Integer code;
    private String value;
    private String desc;

    OrderOriginTypeEnum(Integer code, String value, String desc) {
        this.code = code;
        this.value = value;
        this.desc = desc;
    }

    /***选项类型*/
    public static List<EnumItemInfo> SELECT_LIST = new ArrayList<>();
    /***code-enum map*/
    public static Map<Integer, OrderOriginTypeEnum> CODE_ENUM_MAP = new LinkedHashMap<>(16);
    /***value-enum map*/
    public static Map<String, OrderOriginTypeEnum> VALUE_ENUM_MAP = new LinkedHashMap<>(16);

    static {
        for (OrderOriginTypeEnum item :
                OrderOriginTypeEnum.values()) {
            SELECT_LIST.add(new EnumItemInfo(item.getCode(), item.getValue(), item.getDesc()));
            CODE_ENUM_MAP.put(item.getCode(), item);
            VALUE_ENUM_MAP.put(item.getValue(), item);
        }
    }

    public static OrderOriginTypeEnum getEnum(Object object) {
        if (object != null) {
            return VALUE_ENUM_MAP.get(object.toString());
        }
        return null;
    }

    public static String getEnumDesc(Object object) {
        if (object != null) {
            OrderOriginTypeEnum anEnum = VALUE_ENUM_MAP.get(object.toString());
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
