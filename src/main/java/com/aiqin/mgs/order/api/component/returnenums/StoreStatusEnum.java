package com.aiqin.mgs.order.api.component.returnenums;

/**
 * 支调用门店退货申请-完成(门店)（erp回调）---订货管理-修改退货申请单
 * 
 * @author hantao
 *
 */
public enum StoreStatusEnum {

    PAY_ORDER_TYPE_ZHI(1, "审核通过"),
    PAY_ORDER_TYPE_PEI(2, "审核不通过（挂账）");

    private Integer key;
    private String msg;

    StoreStatusEnum(Integer key, String msg) {
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
