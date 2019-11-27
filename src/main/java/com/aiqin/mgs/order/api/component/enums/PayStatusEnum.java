package com.aiqin.mgs.order.api.component.enums;

import com.aiqin.mgs.order.api.domain.EnumItemInfo;
import lombok.Getter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 支付状态枚举类
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/11/12 16:58
 */
@Getter
public enum PayStatusEnum {

    /***待支付*/
    UNPAID(0, "0", "待支付"),
    /***已发起支付*/
    PAYING(1, "1", "已发起支付"),
    /***支付成功*/
    SUCCESS(2, "2", "支付成功"),
    /***支付失败*/
    FAIL(3, "3", "支付失败");

    private Integer code;
    private String value;
    private String desc;

    PayStatusEnum(Integer code, String value, String desc) {
        this.code = code;
        this.value = value;
        this.desc = desc;
    }

    /***选项类型*/
    public static List<EnumItemInfo> SELECT_LIST = new ArrayList<>();
    /***code-enum map*/
    public static Map<Integer, PayStatusEnum> CODE_ENUM_MAP = new LinkedHashMap<>(16);
    /***value-enum map*/
    public static Map<String, PayStatusEnum> VALUE_ENUM_MAP = new LinkedHashMap<>(16);

    static {
        for (PayStatusEnum item :
                PayStatusEnum.values()) {
            SELECT_LIST.add(new EnumItemInfo(item.getCode(), item.getValue(), item.getDesc()));
            CODE_ENUM_MAP.put(item.getCode(), item);
            VALUE_ENUM_MAP.put(item.getValue(), item);
        }
    }

    public static PayStatusEnum getEnum(Object object) {
        if (object != null) {
            return VALUE_ENUM_MAP.get(object.toString());
        }
        return null;
    }

    public static String getEnumDesc(Object object) {
        if (object != null) {
            PayStatusEnum anEnum = VALUE_ENUM_MAP.get(object.toString());
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
