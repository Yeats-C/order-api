package com.aiqin.mgs.order.api.component.enums;

import com.aiqin.mgs.order.api.component.enums.pay.ErpRequestPayTransactionTypeEnum;
import lombok.Getter;

/**
 * 订单类型节点差异控制枚举类
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/12/18 16:48
 */
@Getter
public enum ErpOrderNodeProcessTypeEnum {

    /***配送	- 普通首单*/
    PROCESS_1(ErpOrderTypeEnum.DISTRIBUTION, ErpOrderCategoryEnum.ORDER_TYPE_2, ErpRequestPayTransactionTypeEnum.FIRST_DELIVERY, true, true, true, false, true, true, false, true, true, true, false, true),
    /***配送	- 首单赠送*/
    PROCESS_2(ErpOrderTypeEnum.DISTRIBUTION, ErpOrderCategoryEnum.ORDER_TYPE_4, ErpRequestPayTransactionTypeEnum.FIRST_DELIVERY, true, true, false, false, true, true, false, true, true, true, false, true),
    /***配送	- 正常补货*/
    PROCESS_3(ErpOrderTypeEnum.DISTRIBUTION, ErpOrderCategoryEnum.ORDER_TYPE_1, ErpRequestPayTransactionTypeEnum.DELIVERY_REPLENISHMENT, true, true, true, true, true, true, true, true, false, true, true, true),
    /***直送	- 正常补货*/
    PROCESS_4(ErpOrderTypeEnum.DIRECT_SEND, ErpOrderCategoryEnum.ORDER_TYPE_1, ErpRequestPayTransactionTypeEnum.DIRECT_DELIVERY_REPLENISHMENT, true, false, true, false, false, false, false, false, false, false, false, false),
    /***直送	- 普通首单*/
    PROCESS_5(ErpOrderTypeEnum.DIRECT_SEND, ErpOrderCategoryEnum.ORDER_TYPE_2, ErpRequestPayTransactionTypeEnum.FIRST_DIRECT_DELIVERY, true, false, true, false, false, false, false, false, true, false, false, false),
    /***辅采直送	- 新店货架*/
    PROCESS_6(ErpOrderTypeEnum.ASSIST_PURCHASING, ErpOrderCategoryEnum.ORDER_TYPE_16, ErpRequestPayTransactionTypeEnum.FIRST_DIRECT_DELIVERY, true, false, false, false, false, false, false, false, true, false, false, false),
    /***辅采直送	- 货架补货*/
    PROCESS_7(ErpOrderTypeEnum.ASSIST_PURCHASING, ErpOrderCategoryEnum.ORDER_TYPE_17, ErpRequestPayTransactionTypeEnum.DIRECT_DELIVERY_REPLENISHMENT, true, false, false, false, false, false, false, false, true, false, false, false),
    /***辅采直送	- 游泳游乐*/
    PROCESS_8(ErpOrderTypeEnum.ASSIST_PURCHASING, ErpOrderCategoryEnum.ORDER_TYPE_172, ErpRequestPayTransactionTypeEnum.DIRECT_DELIVERY_REPLENISHMENT, true, false, false, false, false, false, false, false, true, false, false, false),
     /***配送	- 批发订货*/
    PROCESS_9(ErpOrderTypeEnum.DISTRIBUTION, ErpOrderCategoryEnum.ORDER_TYPE_51, ErpRequestPayTransactionTypeEnum.DIRECT_DELIVERY_REPLENISHMENT, true, false, false, false, false, false, false, false, true, true, false, true),
    /***采购直送	- 采购直送*/
    PROCESS_10(ErpOrderTypeEnum.DIRECT_PURCHASE, ErpOrderCategoryEnum.ORDER_TYPE_147, ErpRequestPayTransactionTypeEnum.DIRECT_DELIVERY_REPLENISHMENT, true, false, false, false, false, false, false, false, true, false, false, false),
    /***采购直送	- 首单赠送*/
    PROCESS_11(ErpOrderTypeEnum.DIRECT_PURCHASE, ErpOrderCategoryEnum.ORDER_TYPE_4, ErpRequestPayTransactionTypeEnum.DIRECT_DELIVERY_REPLENISHMENT, true, false, false, false, false, false, false, false, true, false, false, false),
    ;

    ErpOrderNodeProcessTypeEnum(ErpOrderTypeEnum orderTypeEnum, ErpOrderCategoryEnum orderCategoryEnum, ErpRequestPayTransactionTypeEnum payTransactionTypeEnum, boolean areaCheck, boolean repertoryCheck, boolean priceCheck, boolean activityCheck, boolean hasActivity, boolean hasCoupon, boolean addProductGift, boolean hasLogisticsFee, boolean autoPay, boolean lockStock, boolean hasGoodsCoupon, boolean splitByRepertory) {
        this.orderTypeEnum = orderTypeEnum;
        this.orderCategoryEnum = orderCategoryEnum;
        this.payTransactionTypeEnum = payTransactionTypeEnum;
        this.areaCheck = areaCheck;
        this.repertoryCheck = repertoryCheck;
        this.priceCheck = priceCheck;
        this.activityCheck = activityCheck;
        this.hasActivity = hasActivity;
        this.hasCoupon = hasCoupon;
        this.addProductGift = addProductGift;
        this.hasLogisticsFee = hasLogisticsFee;
        this.autoPay = autoPay;
        this.lockStock = lockStock;
        this.hasGoodsCoupon = hasGoodsCoupon;
        this.splitByRepertory = splitByRepertory;
    }

    /***订单类型枚举*/
    private ErpOrderTypeEnum orderTypeEnum;
    /***订单类别枚举*/
    private ErpOrderCategoryEnum orderCategoryEnum;
    /***结算中心业务类型*/
    private ErpRequestPayTransactionTypeEnum payTransactionTypeEnum;
    /***商品销售区域配置校验*/
    private boolean areaCheck;
    /***商品库存校验*/
    private boolean repertoryCheck;
    /***商品销售价格校验*/
    private boolean priceCheck;
    /***促销活动校验*/
    private boolean activityCheck;
    /***可选活动活动*/
    private boolean hasActivity;
    /***可选优惠券*/
    private boolean hasCoupon;
    /***待支付状态增加赠品行*/
    private boolean addProductGift;
    /***需要支付物流费用*/
    private boolean hasLogisticsFee;
    /***自动发起支付*/
    private boolean autoPay;
    /***需要锁库存/解锁库存*/
    private boolean lockStock;
    /***支付成功返回物流券*/
    private boolean hasGoodsCoupon;
    /***按照库房拆单 1是 0否（按照供应商）*/
    private boolean splitByRepertory;


    /**
     * 根据订单类型编码和订单类别编码获取订单类型节点差异控制枚举类
     *
     * @param orderTypeCode     订单类型编码
     * @param orderCategoryCode 订单类别编码
     * @return
     */
    public static ErpOrderNodeProcessTypeEnum getEnum(Object orderTypeCode, Object orderCategoryCode) {
        if (orderTypeCode != null && orderCategoryCode != null) {
            ErpOrderTypeEnum orderTypeEnum = ErpOrderTypeEnum.getEnum(orderTypeCode.toString());
            ErpOrderCategoryEnum orderCategoryEnum = ErpOrderCategoryEnum.getEnum(orderCategoryCode.toString());
            if (orderTypeEnum != null && orderCategoryEnum != null) {

                for (ErpOrderNodeProcessTypeEnum item :
                        ErpOrderNodeProcessTypeEnum.values()) {
                    if (orderTypeEnum == item.getOrderTypeEnum() && orderCategoryEnum == item.getOrderCategoryEnum()) {
                        return item;
                    }
                }
            }
        }
        return null;
    }
}
