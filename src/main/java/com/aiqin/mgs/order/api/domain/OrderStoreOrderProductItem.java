package com.aiqin.mgs.order.api.domain;

import com.aiqin.mgs.order.api.component.enums.ProductGiftEnum;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单商品明细
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/11/14 10:59
 */
@Data
public class OrderStoreOrderProductItem {

    /***主键*/
    private Long id;
    /***订单id*/
    private String orderId;
    /***订单编号*/
    private String orderCode;
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

    /***本品赠品标记 ProductGiftEnum */
    private Integer productGift;
    /***本品赠品描述 ProductGiftEnum */
    private String productGiftDesc;
    /***赠品行关联本品行编码*/
    private String parentOrderItemCode;

    /***活动id*/
    private String activityId;
    /***服纺券id*/
    private String spinCouponId;
    /***A品券id*/
    private String topCouponId;

    /***订货数量*/
    private Integer quantity;
    /***仓库实发数量*/
    private Integer actualDeliverQuantity;
    /***仓库发货数量差异原因*/
    private String deliverDifferenceReason;
    /***门店实收数量*/
    private Integer actualStoreQuantity;
    /***签收数量差异原因*/
    private String signDifferenceReason;

    /***订货价*/
    private BigDecimal price;
    /***含税采购价*/
    private BigDecimal taxPurchasePrice;
    /***分摊后单价*/
    private BigDecimal sharePrice;

    /***订货金额*/
    private BigDecimal money;
    /***实际支付金额*/
    private BigDecimal realMoney;
    /***活动优惠金额*/
    private BigDecimal activityMoney;
    /***服纺券优惠金额*/
    private BigDecimal spinCouponMoney;
    /***A品券优惠金额*/
    private BigDecimal topCouponMoney;

    /***创建时间*/
    private Date createTime;
    /***创建人id*/
    private String createById;
    /***创建人姓名*/
    private String createByName;
    /***更新时间*/
    private Date updateTime;
    /***修改人id*/
    private String updateById;
    /***修改人姓名*/
    private String updateByName;

    public String getProductGiftDesc() {
        return ProductGiftEnum.getEnumDesc(productGift);
    }
}
