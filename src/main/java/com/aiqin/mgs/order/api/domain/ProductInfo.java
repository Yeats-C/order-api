package com.aiqin.mgs.order.api.domain;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

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
    /***供应商编码*/
    private String supplierCode;
    /***供应商名称*/
    private String supplierName;
    /***单位*/
    private String unit;
    /***单价*/
    private BigDecimal price;
    /***含税采购价*/
    private BigDecimal taxPurchasePrice;

    /***商品在各个仓库的数量*/
    private List<ProductRepertoryQuantity> repertoryQuantityList;

    //TODO 字段待补充

}
