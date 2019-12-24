package com.aiqin.mgs.order.api.component.enums;

import lombok.Getter;

/**
 * 订单类型类别查询类型枚举
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/12/23 11:21
 */
@Getter
public enum ErpOrderTypeCategoryQueryTypeEnum {

    /***运营ERP查询销售单列表*/
    ERP_ORDER_LIST_QUERY(1, "1", "运营ERP查询销售单列表"),
    /***运营ERP查询货架订单列表*/
    ERP_RACK_ORDER_LIST_QUERY(2, "2", "运营ERP查询货架订单列表"),
    /***运营ERP查询购物车可创建订单类型类别*/
    ERP_CART_ORDER_CREATE(3, "3", "运营ERP查询购物车可创建订单类型类别"),
    /***运营ERP查询货架订单可创建订单类型类别*/
    ERP_RACK_ORDER_CREATE(4, "4", "运营ERP查询货架订单可创建订单类型类别"),
    /***爱掌柜查询订单列表订单类型类别*/
    STORE_ORDER_LIST_QUERY(5, "5", "爱掌柜查询订单列表订单类型类别"),
    /***爱掌柜查询购物车可创建订单类型类别*/
    STORE_CART_ORDER_CREATE(6, "6", "爱掌柜查询购物车可创建订单类型类别"),
    ;

    /***数字类型状态*/
    private Integer code;
    /***字符串类型状态*/
    private String value;
    /***状态描述*/
    private String desc;

    ErpOrderTypeCategoryQueryTypeEnum(Integer code, String value, String desc) {
        this.code = code;
        this.value = value;
        this.desc = desc;
    }


    public static ErpOrderTypeCategoryQueryTypeEnum getEnum(Object object) {
        if (object != null) {
            for (ErpOrderTypeCategoryQueryTypeEnum item :
                    ErpOrderTypeCategoryQueryTypeEnum.values()) {
                if (item.getValue().equals(object.toString())) {
                    return item;
                }
            }
        }
        return null;
    }

}