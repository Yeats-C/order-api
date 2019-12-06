package com.aiqin.mgs.order.api.component.enums;

import com.aiqin.mgs.order.api.domain.EnumItemInfo;
import lombok.Getter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 商品本品赠品枚举类
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/11/15 17:33
 */
@Getter
public enum ProductGiftEnum {

    /***本品*/
    PRODUCT(1, "1", "本品"),
    /***赠品*/
    GIFT(2, "2", "赠品");

    private Integer code;
    private String value;
    private String desc;

    ProductGiftEnum(Integer code, String value, String desc) {
        this.code = code;
        this.value = value;
        this.desc = desc;
    }

    /***选项类型*/
    public static final List<EnumItemInfo> SELECT_LIST = new ArrayList<>();
    /***code-enum map*/
    public static final Map<Integer, ProductGiftEnum> CODE_ENUM_MAP = new LinkedHashMap<>(16);
    /***value-enum map*/
    public static final Map<String, ProductGiftEnum> VALUE_ENUM_MAP = new LinkedHashMap<>(16);

    static {
        for (ProductGiftEnum item :
                ProductGiftEnum.values()) {
            SELECT_LIST.add(new EnumItemInfo(item.getCode(), item.getValue(), item.getDesc()));
            CODE_ENUM_MAP.put(item.getCode(), item);
            VALUE_ENUM_MAP.put(item.getValue(), item);
        }
    }

    /**
     * 返回枚举对象
     * @param object
     * @return
     */
    public static ProductGiftEnum getEnum(Object object) {
        if (object != null) {
            return VALUE_ENUM_MAP.get(object.toString());
        }
        return null;
    }

    /**
     * 返回枚举对象的描述信息
     * @param object
     * @return
     */
    public static String getEnumDesc(Object object) {
        if (object != null) {
            ProductGiftEnum anEnum = VALUE_ENUM_MAP.get(object.toString());
            if (anEnum != null) {
                return anEnum.getDesc();
            }
        }
        return "";
    }

    /**
     * 返回枚举对象的值
     * @param object
     * @return
     */
    public static String getEnumValue(Object object) {
        if (object != null) {
            ProductGiftEnum productGiftEnum = VALUE_ENUM_MAP.get(object.toString());
            if (productGiftEnum != null){
                return productGiftEnum.getValue();
            }
        }
        return "";
    }

    /**
     * 返回枚举对象的编码
     * @param object
     * @return
     */
    public static Integer getEnumCode(Object object){
        if (object != null){
            ProductGiftEnum productGiftEnum = CODE_ENUM_MAP.get(object.toString());
            if (productGiftEnum != null){
                return productGiftEnum.getCode();
            }
        }
        return null;
    }

    /**
     * 判断枚举类型是否存在
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
