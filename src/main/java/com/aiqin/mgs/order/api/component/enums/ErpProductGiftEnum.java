package com.aiqin.mgs.order.api.component.enums;

import com.aiqin.mgs.order.api.domain.EnumItemInfo;
import lombok.Getter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 商品本品赠品枚举
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/11/15 17:33
 */
@Getter
public enum ErpProductGiftEnum {

    /***商品*/
    PRODUCT(0, "0", "商品"),
    /***赠品*/
    GIFT(1, "1", "赠品");

    private Integer code;
    private String value;
    private String desc;

    ErpProductGiftEnum(Integer code, String value, String desc) {
        this.code = code;
        this.value = value;
        this.desc = desc;
    }

    /***选项类型*/
    public static final List<EnumItemInfo> SELECT_LIST = new ArrayList<>();
    /***value-enum map*/
    public static final Map<String, ErpProductGiftEnum> VALUE_ENUM_MAP = new LinkedHashMap<>(16);

    static {
        for (ErpProductGiftEnum item :
                ErpProductGiftEnum.values()) {
            SELECT_LIST.add(new EnumItemInfo(item.getCode(), item.getValue(), item.getDesc()));
            VALUE_ENUM_MAP.put(item.getValue(), item);
        }
    }

    /**
     * 返回枚举对象
     *
     * @param object
     * @return
     */
    public static ErpProductGiftEnum getEnum(Object object) {
        if (object != null) {
            return VALUE_ENUM_MAP.get(object.toString());
        }
        return null;
    }

    /**
     * 返回枚举对象的描述信息
     *
     * @param object
     * @return
     */
    public static String getEnumDesc(Object object) {
        if (object != null) {
            ErpProductGiftEnum anEnum = VALUE_ENUM_MAP.get(object.toString());
            if (anEnum != null) {
                return anEnum.getDesc();
            }
        }
        return "";
    }

    /**
     * 判断枚举类型是否存在
     *
     * @param object
     * @return
     */
    public static boolean exist(Object object) {
        if (object != null) {
            return VALUE_ENUM_MAP.containsKey(object.toString());
        }
        return false;
    }

}
