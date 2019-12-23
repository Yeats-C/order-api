package com.aiqin.mgs.order.api.component.enums;

import com.aiqin.mgs.order.api.domain.EnumItemInfo;
import lombok.Getter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 日志来源类型
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/12/19 20:35
 */
@Getter
public enum ErpLogSourceTypeEnum {

    /***销售*/
    SALES(0, "0", "销售"),
    /***采购*/
    PURCHASE(1, "1", "采购"),
    /***退货*/
    RETURN(2, "2", "退货"),
    /***退供*/
    RETURN_SUPPLY(3, "3", "退供"),
    ;

    private Integer code;
    private String value;
    private String desc;

    ErpLogSourceTypeEnum(Integer code, String value, String desc) {
        this.code = code;
        this.value = value;
        this.desc = desc;
    }

    /***选项类型*/
    public static final List<EnumItemInfo> SELECT_LIST = new ArrayList<>();
    /***value-enum map*/
    public static final Map<String, ErpLogSourceTypeEnum> VALUE_ENUM_MAP = new LinkedHashMap<>(16);

    static {
        for (ErpLogSourceTypeEnum item :
                ErpLogSourceTypeEnum.values()) {
            SELECT_LIST.add(new EnumItemInfo(item.getCode(), item.getValue(), item.getDesc()));
            VALUE_ENUM_MAP.put(item.getValue(), item);
        }
    }

    public static ErpLogSourceTypeEnum getEnum(Object object) {
        if (object != null) {
            return VALUE_ENUM_MAP.get(object.toString());
        }
        return null;
    }

    public static String getEnumDesc(Object object) {
        if (object != null) {
            ErpLogSourceTypeEnum anEnum = VALUE_ENUM_MAP.get(object.toString());
            if (anEnum != null) {
                return anEnum.getDesc();
            }
        }
        return "";
    }

}
