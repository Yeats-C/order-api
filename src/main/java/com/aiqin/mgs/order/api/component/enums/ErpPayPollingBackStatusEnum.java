package com.aiqin.mgs.order.api.component.enums;

import com.aiqin.mgs.order.api.domain.EnumItemInfo;
import lombok.Getter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 支付单轮询返回状态
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/11/21 10:12
 */
@Getter
public enum ErpPayPollingBackStatusEnum {

    /***订单支付处理中*/
    STATUS_0(0, "0", "订单支付处理中"),
    /***订单支付成功*/
    STATUS_1(1, "1", "订单支付成功"),
    /***订单支付失败*/
    STATUS_2(2, "2", "订单支付失败"),
    /***订单退款中*/
    STATUS_3(3, "3", "订单退款中"),
    /***订单退款成功*/
    STATUS_4(4, "4", "订单退款成功"),
    /***订单退款失败*/
    STATUS_5(5, "5", "订单退款失败"),
    /***订单未支付*/
    STATUS_6(6, "6", "订单未支付"),
    /***订单超时*/
    STATUS_7(7, "7", "订单超时"),
    /***订单取消*/
    STATUS_8(8, "8", "订单取消"),
    /***订单支付失败卡已销*/
    STATUS_9(9, "9", "订单支付失败卡已销");

    private Integer code;
    private String value;
    private String desc;

    ErpPayPollingBackStatusEnum(Integer code, String value, String desc) {
        this.code = code;
        this.value = value;
        this.desc = desc;
    }

    /***选项类型*/
    public static final List<EnumItemInfo> SELECT_LIST = new ArrayList<>();
    /***value-enum map*/
    public static final Map<String, ErpPayPollingBackStatusEnum> VALUE_ENUM_MAP = new LinkedHashMap<>(16);

    static {
        for (ErpPayPollingBackStatusEnum item :
                ErpPayPollingBackStatusEnum.values()) {
            SELECT_LIST.add(new EnumItemInfo(item.getCode(), item.getValue(), item.getDesc()));
            VALUE_ENUM_MAP.put(item.getValue(), item);
        }
    }

    public static ErpPayPollingBackStatusEnum getEnum(Object object) {
        if (object != null) {
            return VALUE_ENUM_MAP.get(object.toString());
        }
        return null;
    }

    public static String getEnumDesc(Object object) {
        if (object != null) {
            ErpPayPollingBackStatusEnum anEnum = VALUE_ENUM_MAP.get(object.toString());
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
