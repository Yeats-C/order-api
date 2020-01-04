package com.aiqin.mgs.order.api.component.enums;

import com.aiqin.mgs.order.api.component.enums.pay.ErpRequestPayOriginTypeEnum;
import lombok.Getter;

/**
 * 订单类型枚举
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/11/15 11:09
 */
@Getter
public enum ErpOrderTypeEnum {

    /***直送*/
    DIRECT_SEND(1, "1", "直送", 11, 2),
    /***配送*/
    DISTRIBUTION(2, "2", "配送", 7, 14),
    /***辅采直送*/
    ASSIST_PURCHASING(3, "3", "辅采直送", 7, 14);

    private Integer code;
    private String value;
    private String desc;
    /***发起支付参数支付来源*/
    private Integer payOriginType;
    /***发起支付参数订单类型*/
    private Integer payOrderType;

    ErpOrderTypeEnum(Integer code, String value, String desc, Integer payOriginType, Integer payOrderType) {
        this.code = code;
        this.value = value;
        this.desc = desc;
        this.payOriginType = payOriginType;
        this.payOrderType = payOrderType;
    }

    /**
     * 根据值获取枚举类
     *
     * @param object
     * @return
     */
    public static ErpOrderTypeEnum getEnum(Object object) {
        if (object != null) {
            for (ErpOrderTypeEnum item :
                    ErpOrderTypeEnum.values()) {
                if (item.getValue().equals(object.toString())) {
                    return item;
                }
            }
        }
        return null;
    }

    /**
     * 根据枚举类的值获取枚举的描述
     *
     * @param object
     * @return
     */
    public static String getEnumDesc(Object object) {
        if (object != null) {
            ErpOrderTypeEnum anEnum = getEnum(object.toString());
            if (anEnum != null) {
                return anEnum.getDesc();
            }
        }
        return "";
    }


    /**
     * 校验值是否存在
     *
     * @param object
     * @return
     */
    public static boolean exist(Object object) {
        if (object != null) {
            ErpOrderTypeEnum anEnum = getEnum(object.toString());
            if (anEnum != null) {
                return true;
            }
        }
        return false;
    }
}
