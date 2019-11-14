package com.aiqin.mgs.order.api.domain;

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

    /***订单商品ID*/
    private String orderProductId;

    /***商品sku码*/
    private String skuCode;

    /***商品名称*/
    private String skuName;

    /***订货数量*/
    private Integer productNumber;

    /***订货价*/
    private BigDecimal productOrderPrice;

    /***订货金额*/
    private BigDecimal orderMoney;

    /***单位*/
    private String unit;

    /***仓库实发数量*/
    private Integer actualDeliverNum;

    /***门店实收数量*/
    private Integer actualStoreNum;

    /***商品单价*/
    private BigDecimal originalProductPrice;

    /***分摊后单价*/
    private BigDecimal shareAfterPrice;

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
}
