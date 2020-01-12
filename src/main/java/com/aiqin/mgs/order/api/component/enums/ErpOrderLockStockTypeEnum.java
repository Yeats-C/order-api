package com.aiqin.mgs.order.api.component.enums;

import lombok.Getter;

/**
 * 库存操作类型枚举类
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/12/23 15:17
 */
@Getter
public enum ErpOrderLockStockTypeEnum {

    /***锁定库存数*/
    LOCK(1, "1", "锁定库存数"),
    /***减少库存并解锁 支付成功后解锁*/
    REDUCE_AND_UNLOCK(2, "2", "减少库存并解锁"),
    /***解锁锁定库存 支付成功之前解锁*/
    UNLOCK(3, "3", "解锁锁定库存"),
    ;

    private Integer code;
    private String value;
    private String desc;

    ErpOrderLockStockTypeEnum(Integer code, String value, String desc) {
        this.code = code;
        this.value = value;
        this.desc = desc;
    }

    public static ErpOrderLockStockTypeEnum getEnum(Object object) {
        if (object != null) {
            for (ErpOrderLockStockTypeEnum item :
                    ErpOrderLockStockTypeEnum.values()) {
                if (item.getValue().equals(object.toString())) {
                    return item;
                }
            }
        }
        return null;
    }

}
