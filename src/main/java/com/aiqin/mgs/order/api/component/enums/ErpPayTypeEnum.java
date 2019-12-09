package com.aiqin.mgs.order.api.component.enums;

import com.aiqin.mgs.order.api.domain.EnumItemInfo;
import lombok.Getter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 支付类型枚举
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/11/12 17:15
 */
@Getter
public enum ErpPayTypeEnum {

    /***在线支付-微信*/
    ONLINE_WE_CHAT(0, "0", "在线支付-微信"),
    /***在线支付-支付宝*/
    ONLINE_ALI_PAY(1, "1", "在线支付-支付宝"),
    /***在线支付-银行卡*/
    ONLINE_BANK_CARD(2, "2", "在线支付-银行卡"),
    /***到店支付-现金*/
    CASH(3, "3", "到店支付-现金"),
    /***到店支付-微信*/
    DOOR_WE_CHAT(4, "4", "到店支付-微信"),
    /***到店支付-支付宝*/
    DOOR_ALI_PAY(5, "5", "到店支付-支付宝"),
    /***到店支付-银行卡*/
    DOOR_BANK_CARD(6, "6", "到店支付-银行卡"),
    /***储值卡支付*/
    BALANCE_PAY(7, "7", "储值卡支付");

    private Integer code;
    private String value;
    private String desc;

    ErpPayTypeEnum(Integer code, String value, String desc) {
        this.code = code;
        this.value = value;
        this.desc = desc;
    }

    /***选项类型*/
    public static final List<EnumItemInfo> SELECT_LIST = new ArrayList<>();
    /***code-enum map*/
    public static final Map<Integer, ErpPayTypeEnum> CODE_ENUM_MAP = new LinkedHashMap<>(16);
    /***value-enum map*/
    public static final Map<String, ErpPayTypeEnum> VALUE_ENUM_MAP = new LinkedHashMap<>(16);

    static {
        for (ErpPayTypeEnum item :
                ErpPayTypeEnum.values()) {
            SELECT_LIST.add(new EnumItemInfo(item.getCode(), item.getValue(), item.getDesc()));
            CODE_ENUM_MAP.put(item.getCode(), item);
            VALUE_ENUM_MAP.put(item.getValue(), item);
        }
    }

    public static ErpPayTypeEnum getEnum(Object object) {
        if (object != null) {
            return VALUE_ENUM_MAP.get(object.toString());
        }
        return null;
    }

    public static String getEnumDesc(Object object) {
        if (object != null) {
            ErpPayTypeEnum anEnum = VALUE_ENUM_MAP.get(object.toString());
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
