package com.aiqin.mgs.order.api.component.enums;

import lombok.Getter;

/**
 * 订单节点流程控制状态枚举类
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2020/1/1 11:34
 */
@Getter
public enum ErpOrderNodeStatusEnum {

    /****/
    STATUS_1(1, "1", "创建成功，等待发起支付"),
    STATUS_2(2, "2", "已发起支付，等待结果"),
    STATUS_3(3, "3", "支付成功，等待获取物流券"),
    STATUS_4(4, "4", "支付失败"),
    STATUS_5(5, "5", "获取物流券成功，等待拆单"),
    STATUS_6(6, "6", "拆单成功，等待同步到供应链"),
    //STATUS_7(7, "7", "同步到供应链成功"),
    STATUS_8(8, "8", "同步供应链成功，等待出货"),
    STATUS_9(9, "9", "已出货，等待发运"),
    STATUS_10(10, "10", "已发运等待支付物流费用"),
    STATUS_11(11, "11", "已支付物流费用(或者不需要支付物流费用)，等待签收"),
    STATUS_12(12, "12", "已签收"),
    STATUS_21(21, "21", "超时未支付成功取消"),
    STATUS_22(22, "22", "交易异常终止"),
    STATUS_23(23, "23", "缺货取消"),

    /*** 31~39 表示取消订单退款流程 */
    STATUS_31(31, "31", "申请取消，等待注销订单关联优惠券"),
    STATUS_32(32, "32", "注销券成功，等待解锁库存"),
    STATUS_33(33, "33", "解锁库存成功，等待取消采购单"),
    STATUS_34(34, "34", "取消采购单成功，等待生成退款单"),
    STATUS_35(35, "35", "生成退款单成功，等待发起退款"),
    STATUS_36(36, "36", "发起退款成功，等待退款结果"),
    STATUS_37(37, "37", "退款成功"),
    STATUS_38(38, "38", "退款失败"),
    ;

    private Integer code;
    private String value;
    private String desc;

    ErpOrderNodeStatusEnum(Integer code, String value, String desc) {
        this.code = code;
        this.value = value;
        this.desc = desc;
    }

    /**
     * 根据值获取枚举类
     *
     * @param object
     * @return
     */
    public static ErpOrderNodeStatusEnum getEnum(Object object) {
        if (object != null) {
            for (ErpOrderNodeStatusEnum item :
                    ErpOrderNodeStatusEnum.values()) {
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
            ErpOrderNodeStatusEnum anEnum = getEnum(object.toString());
            if (anEnum != null) {
                return anEnum.getDesc();
            }
        }
        return "";
    }

}
