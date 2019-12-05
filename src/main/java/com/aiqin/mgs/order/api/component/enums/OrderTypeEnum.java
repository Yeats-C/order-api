package com.aiqin.mgs.order.api.component.enums;

import com.aiqin.mgs.order.api.domain.EnumItemInfo;
import lombok.Getter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 订单类型枚举类
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/11/12 17:00
 */
@Getter
public enum OrderTypeEnum {

    /***首单配送*/
    ORDER_TYPE_1(1, "1", "首单配送", ProductTypeEnum.DISTRIBUTION, true, true, true, false),
    /***首单赠送*/
    ORDER_TYPE_2(2, "2", "首单赠送", ProductTypeEnum.DISTRIBUTION, true, true, false, false),
    /***首单货架*/
    ORDER_TYPE_3(3, "3", "首单货架", ProductTypeEnum.STORAGE_RACK, true, false, false, false),
    /***货架补货*/
    ORDER_TYPE_4(4, "4", "货架补货", ProductTypeEnum.STORAGE_RACK, true, false, false, false),
    /***配送补货*/
    ORDER_TYPE_5(5, "5", "配送补货", ProductTypeEnum.DISTRIBUTION, true, true, true, true),
    /***游乐设备*/
    ORDER_TYPE_6(6, "6", "游乐设备", ProductTypeEnum.STORAGE_RACK, true, false, false, false),
    /***首单直送*/
    ORDER_TYPE_7(7, "7", "首单直送", ProductTypeEnum.DIRECT_SEND, true, false, true, false),
    /***直送补货*/
    ORDER_TYPE_8(8, "8", "直送补货", ProductTypeEnum.DIRECT_SEND, true, false, true, false);

    /***数字编码*/
    private Integer code;
    /***字符串编码*/
    private String value;
    /***描述*/
    private String desc;
    /***订单商品类型*/
    private ProductTypeEnum productTypeEnum;
    /***商品销售区域配置校验*/
    private boolean areaCheck;
    /***商品库存校验*/
    private boolean repertoryCheck;
    /***商品销售价格校验*/
    private boolean priceCheck;
    /***促销活动校验*/
    private boolean activityCheck;

    OrderTypeEnum(Integer code, String value, String desc, ProductTypeEnum productTypeEnum, boolean areaCheck, boolean repertoryCheck, boolean priceCheck, boolean activityCheck) {
        this.code = code;
        this.value = value;
        this.desc = desc;
        this.productTypeEnum = productTypeEnum;
        this.areaCheck = areaCheck;
        this.repertoryCheck = repertoryCheck;
        this.priceCheck = priceCheck;
        this.activityCheck = activityCheck;
    }

    /***选项类型*/
    public static List<EnumItemInfo> SELECT_LIST = new ArrayList<>();
    /***code-enum map*/
    public static Map<Integer, OrderTypeEnum> CODE_ENUM_MAP = new LinkedHashMap<>(16);
    /***value-enum map*/
    public static Map<String, OrderTypeEnum> VALUE_ENUM_MAP = new LinkedHashMap<>(16);

    static {
        for (OrderTypeEnum item :
                OrderTypeEnum.values()) {
            SELECT_LIST.add(new EnumItemInfo(item.getCode(), item.getValue(), item.getDesc()));
            CODE_ENUM_MAP.put(item.getCode(), item);
            VALUE_ENUM_MAP.put(item.getValue(), item);
        }
    }

    public static OrderTypeEnum getEnum(Object object) {
        if (object != null) {
            return VALUE_ENUM_MAP.get(object.toString());
        }
        return null;
    }

    public static String getEnumDesc(Object object) {
        if (object != null) {
            OrderTypeEnum anEnum = VALUE_ENUM_MAP.get(object.toString());
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

