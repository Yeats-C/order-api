package com.aiqin.mgs.order.api.component.returnenums;

/**
 * 退货单状态枚举类
 * 
 * @author hantao
 *
 */
public enum ReturnOrderStatusEnum {

    RETURN_ORDER_STATUS_WAIT(1, "待审核"),
    RETURN_ORDER_STATUS_COM(2, "审核通过"),
    RETURN_ORDER_STATUS_COPY(3, "订单同步中"),
    RETURN_ORDER_STATUS_CHECK(4, "等待退货验收"),
    RETURN_ORDER_STATUS_IN(5, "等待退货入库"),
    RETURN_ORDER_STATUS_APPLY(6, "等待审批"),
    RETURN_ORDER_STATUS_RETURN(11, "退货完成"),
    RETURN_ORDER_STATUS_REFUND(12, "退款完成"),
    RETURN_ORDER_STATUS_DONE(97, "退货终止"),
    RETURN_ORDER_STATUS_FALL(98, "审核不通过"),
    RETURN_ORDER_STATUS_REMOVE(99, "已取消");

    private Integer key;
    private String msg;

    ReturnOrderStatusEnum(Integer key, String msg) {
        this.key = key;
        this.msg = msg;
    }

    public Integer getKey() {
        return key;
    }

    public String getMsg() {
        return msg;
    }

}
