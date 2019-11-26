package com.aiqin.mgs.order.api.domain;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 商品信息
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/11/22 17:11
 */
@Data
public class ProductInfo {

    /***商品id*/
    private String productId;
    /***商品编码*/
    private String productCode;
    /***商品名称*/
    private String productName;
    /***skuCode*/
    private String skuCode;
    /***skuName*/
    private String skuName;
    /***单位*/
    private String unit;
    /***单价*/
    private BigDecimal price;
    /***含税采购价*/
    private BigDecimal taxPurchasePrice;

    //TODO 字段待补充

}
