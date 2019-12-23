package com.aiqin.mgs.order.api.component.enums;

import com.aiqin.mgs.order.api.domain.EnumItemInfo;
import lombok.Getter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 发票类型枚举类
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/12/18 20:24
 */
@Getter
public enum ErpInvoiceTypeEnum {

    /***不开发票*/
    NO_INVOICE(1, "1", "不开发票"),
    /***普通增值税发票*/
    ORDINARY_VAT(2, "2", "普通增值税发票"),
    /***专用增值税发票*/
    SPECIAL_VAT(3, "3", "专用增值税发票");

    /***数字类型状态*/
    private Integer code;
    /***字符串类型状态*/
    private String value;
    /***状态描述*/
    private String desc;

    ErpInvoiceTypeEnum(Integer code, String value, String desc) {
        this.code = code;
        this.value = value;
        this.desc = desc;
    }
    /***选项类型*/
    public static final List<EnumItemInfo> SELECT_LIST = new ArrayList<>();
    /***value-enum map*/
    public static final Map<String, ErpInvoiceTypeEnum> VALUE_ENUM_MAP = new LinkedHashMap<>(16);

    static {
        for (ErpInvoiceTypeEnum item :
                ErpInvoiceTypeEnum.values()) {
            SELECT_LIST.add(new EnumItemInfo(item.getCode(), item.getValue(), item.getDesc()));
            VALUE_ENUM_MAP.put(item.getValue(), item);
        }
    }

    public static ErpInvoiceTypeEnum getEnum(Object object) {
        if (object != null) {
            return VALUE_ENUM_MAP.get(object.toString());
        }
        return null;
    }

    public static String getEnumDesc(Object object) {
        if (object != null) {
            ErpInvoiceTypeEnum anEnum = VALUE_ENUM_MAP.get(object.toString());
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
