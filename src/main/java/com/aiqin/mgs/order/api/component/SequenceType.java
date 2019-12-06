package com.aiqin.mgs.order.api.component;


/**
 * 生成序列类型
 *
 * @author sunx
 */
public enum SequenceType {
    /**
     * 促销活动
     */
    PROMOTION("promotion", "", "", "促销活动"),
    ORDER("order", "", "", "订单"),
    DISCOUNT_AMOUNT("discountAmount", "YH", "", "优惠额"),
    RETURN_REASON("returnReason","","","退货原因"),
    LOGISTICS_REDUCTION("logisticsReduction","","","物流减免序号");
    /**
     * Redis key
     */
    private String name;

    /**
     * 编号 前缀
     */
    private String prefix;

    /**
     * 编号 后缀
     */
    private String suffix;

    /**
     * 描述
     */
    private String description;

    SequenceType(String name, String prefix, String suffix, String description) {
        this.name = name;
        this.prefix = prefix;
        this.suffix = suffix;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getSuffix() {
        return suffix;
    }

    public String getDescription() {
        return description;
    }
}
