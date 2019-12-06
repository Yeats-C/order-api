package com.aiqin.mgs.order.api.component.enums;

import com.aiqin.mgs.order.api.domain.EnumItemInfo;
import lombok.Getter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 购物车商品添加来源
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/11/15 17:30
 */
@Getter
public enum CartProductCreateSourceEnum {

    /***运维中心ERP*/
    ERP(1, "1", "运维中心ERP"),
    /***爱掌柜*/
    STORE(2, "2", "爱掌柜");

    private Integer code;
    private String value;
    private String desc;

    CartProductCreateSourceEnum(Integer code, String value, String desc) {
        this.code = code;
        this.value = value;
        this.desc = desc;
    }

    /***选项类型*/
    public static final List<EnumItemInfo> SELECT_LIST = new ArrayList<>();
    /***code-enum map*/
    public static final Map<Integer, CartProductCreateSourceEnum> CODE_ENUM_MAP = new LinkedHashMap<>(16);
    /***value-enum map*/
    public static final Map<String, CartProductCreateSourceEnum> VALUE_ENUM_MAP = new LinkedHashMap<>(16);

    static {
        for (CartProductCreateSourceEnum item :
                CartProductCreateSourceEnum.values()) {
            SELECT_LIST.add(new EnumItemInfo(item.getCode(), item.getValue(), item.getDesc()));
            CODE_ENUM_MAP.put(item.getCode(), item);
            VALUE_ENUM_MAP.put(item.getValue(), item);
        }
    }

    public static CartProductCreateSourceEnum getEnum(Object object) {
        if (object != null) {
            return VALUE_ENUM_MAP.get(object.toString());
        }
        return null;
    }

    public static String getEnumDesc(Object object) {
        if (object != null) {
            CartProductCreateSourceEnum anEnum = VALUE_ENUM_MAP.get(object.toString());
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
