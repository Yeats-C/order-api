package com.aiqin.mgs.order.api.component;

/**
 * 描述:订单状态
 *
 * @author zhujunchao
 * @create 2019-02-14 10:37
 */

public enum OrderStatusEnum {
    OrderStatus_1(1, 1, "待支付", "等待客户支付", "待支付", "客户下单后，等待支付", "您提交了订单，请尽快支付", null, "2", 99),
    OrderStatus_2(2, 2, "付款成功", "付款成功", "已支付", "收款已确认，等待拆分", "您的订单已支付，请等待系统确认", null, "3", 102),
    OrderStatus_3(3, 3, "商品出库", "商品出库", "订单拆分中", "订单拆分过程中", "订单拆分过程中，请等待系统确认", null, "4/5/6", null),
    OrderStatus_4(
            4, 4, "商品出库", "商品出库", "订单同步中", "订单同步采购单、上级销售单、上级采购单", "订单同步过程中，请等待系统确认", null, "5/6", null
    ),
    OrderStatus_5(
            5, 5, "商品出库", "商品出库", "等待配货", "直送订单等待完成", "您的订单等待拣货", null, "6 ", 97
    ),
    OrderStatus_6(
            6, 6, "商品出库", "商品出库", "等待拣货", "配送订单拆分完成，等待拣货", "您的订单等待拣货", null, "7/11", 106
    ),
    OrderStatus_7(
            7, 7, "商品出库", "商品出库", "正在拣货", "打印完拣货单，正在拣货", "您的订单已经开始拣货", null, "8", 107
    ),
    OrderStatus_8(
            8, 8, "商品出库", "商品出库", "扫描完成", "扫描完成，确认订单商品", "您的订单已经开始拣货", null, "11", 108
    ),
    OrderStatus_11(
            9, 11, "待收货", "等待收货", "已全部发货", "已经全部发货", "您的订单已全部发货", null, "12", null
    ),
    OrderStatus_12(
            10, 12, "已签收", "交易完成", "交易全部完成", "交易全部完成，客户收货", "您的订单已完成配送，欢迎您再次光临！", null, null, null
    ),
    OrderStatus_null_1(
            null, null, "已回单", "交易完成", "交易全部完成", null, null, null, null, null
    ),
    OrderStatus_null_2(
            null, null, "已入库", null, null, null, null, null, null, null
    ),
    OrderStatus_96(
            11, 96, "交易取消", "交易取消", "拒收终止交易", "“已全部发货”后客户拒收", "您的订单已终止", null, null, null
    ),
    OrderStatus_97(
            12, 97, "交易取消", "交易取消", "缺货终止交易", "“等待配货”时，因为缺货终止交易", "您的订单已终止", null, null, null
    ),
    OrderStatus_98(
            13, 98, "交易取消", "交易取消", "交易异常终止", "交易异常终止", "您的订单已终止", null, null, null
    ),
    OrderStatus_99(
            14, 99, "交易取消", "交易取消", "已取消", "已取消订单", "您的订单已取消", null, null, null
    ),
    OrderStatus_102(
            15, 102, "申请取消交易", "申请取消交易", "申请终止交易（已支付）", "“已支付”时，客户申请终止交易", "您已申请终止交易，请等待系统确认", null, "98", null
    ),
    OrderStatus_106(
            18, 106, "申请取消交易", "申请取消交易", "申请终止交易（等待拣货）", "“等待拣货”时，客户申请终止交易", "您已申请终止交易，请等待系统确认", null, "98", null
    ),
    OrderStatus_107(
            19, 107, "申请取消交易", "申请取消交易", "申请终止交易（正在拣货）", "“正在拣货”时，客户申请终止交易", "您已申请终止交易，请等待系统确认", null, "98", null
    ),
    OrderStatus_108(
            20, 108, "申请取消交易", "申请取消交易", "申请终止交易（扫描完成）", "“扫描完成”时，客户申请终止交易", "您已申请终止交易，请等待系统确认", null, "98", null
    ),
    OrderStatus_92(
            21, 92, null, null, "待支付订单确认支付", "“待支付”订单人工确认变成“已支付”", "您的订单已支付，请等待系统确认", "否", "2", null
    ),
    OrderStatus_93(
            22, 93, null, null, "已取消订单确认支付", "“已取消”订单人工确认变成“已支付”", "您的订单已支付，请等待系统确认", "否", "2", null
    ),
    OrderStatus_94(
            23, 94, null, null, "申请终止交易不通过", null, "您的订单申请终止交易未通过，继续发货", "否", "原状态", null
    ),
    OrderStatus_95(
            24, 95, null, null, "收货延期", null, "您的订单收货延期", "否", "原状态", null
    );
    /**
     * 序号
     */
    private Integer serial;

    /**
     * 订单状态
     */
    private Integer status;
    /**
     * 前台显示
     */
    private String receptionDisplay;
    /**
     * 前台订单状态
     */
    private String receptionStatus;
    /**
     * 后台订单状态
     */
    private String backstageStatus;
    /**
     * 说明
     */
    private String explain;
    /**
     * 标准描述
     */
    private String standardDescription;
    /**
     * 订单是否显示
     */
    private String displayedIs;
    /**
     * 后续状态
     */
    private String followStatus;
    /**
     * 申请取消、终止订单状态
     */
    private Integer terminationStatus;


    OrderStatusEnum(Integer serial, Integer status, String receptionDisplay, String receptionStatus, String backstageStatus, String explain, String standardDescription, String displayedIs, String followStatus, Integer terminationStatus) {
        this.serial = serial;
        this.status = status;
        this.receptionDisplay = receptionDisplay;
        this.receptionStatus = receptionStatus;
        this.backstageStatus = backstageStatus;
        this.explain = explain;
        this.standardDescription = standardDescription;
        this.displayedIs = displayedIs;
        this.followStatus = followStatus;
        this.terminationStatus = terminationStatus;
    }

    /**
     * 传对应状态获取状态枚举
     *
     * @param num
     * @return
     */
    public static OrderStatusEnum getOrderStatusEnum(Integer num) {
        for (OrderStatusEnum value : OrderStatusEnum.values()) {
            if (num.equals(value.getStatus())) {
                return value;
            }
        }
        return null;
    }

    public Integer getSerial() {
        return serial;
    }

    public Integer getStatus() {
        return status;
    }

    public String getReceptionDisplay() {
        return receptionDisplay;
    }

    public String getReceptionStatus() {
        return receptionStatus;
    }

    public String getBackstageStatus() {
        return backstageStatus;
    }

    public String getExplain() {
        return explain;
    }

    public String getStandardDescription() {
        return standardDescription;
    }

    public String getDisplayedIs() {
        return displayedIs;
    }

    public String getFollowStatus() {
        return followStatus;
    }

    public Integer getTerminationStatus() {
        return terminationStatus;
    }
}
