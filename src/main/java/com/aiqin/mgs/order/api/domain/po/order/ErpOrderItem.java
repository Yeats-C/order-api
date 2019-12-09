package com.aiqin.mgs.order.api.domain.po.order;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 订单商品明细行实体
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/12/9 10:11
 */
@Data
public class ErpOrderItem extends ErpOrderBase {

    /***订单id*/
    private String orderId;
    /***订单明细id*/
    private String OrderItemId;
    /***订单明细行编号*/
    private String orderItemCode;

    /***商品ID*/
    private String productId;
    /***商品编码*/
    private String productCode;
    /***商品名称*/
    private String productName;
    /***商品sku码*/
    private String skuCode;
    /***商品名称*/
    private String skuName;
    /***单位*/
    private String unit;

    /***活动id*/
    private String activityId;
    /***本品赠品标记 ErpProductGiftEnum */
    private Integer productGift;

    /***订货数量*/
    private Integer quantity;
    /***仓库实际发货数量*/
    private Integer deliverQuantity;
    /***仓库发货数量差异原因*/
    private String deliverDifferenceReason;
    /***门店实际签收数量*/
    private Integer signQuantity;
    /***签收数量差异原因*/
    private String signDifferenceReason;

    /***订货价*/
    private BigDecimal price;
    /***含税采购价*/
    private BigDecimal taxPurchasePrice;

    /***订货金额*/
    private BigDecimal money;
    /***活动优惠金额*/
    private BigDecimal activityMoney;
    /***实际支付金额*/
    private BigDecimal realMoney;
    /***分摊后金额*/
    private BigDecimal shareMoney;
}
