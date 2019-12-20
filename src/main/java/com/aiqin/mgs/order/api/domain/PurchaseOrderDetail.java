package com.aiqin.mgs.order.api.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
/**
 * 采购商品信息
 * TableName: purchase_order_detail
 */
@Data
@ToString(callSuper = true)
@ApiModel("采购商品信息")
public class PurchaseOrderDetail implements Serializable {
    @ApiModelProperty(value = "业务id")
    private String purchaseOrderDetailId;
    @ApiModelProperty(value = "采购单号")
    private String purchaseOrderCode;
    @ApiModelProperty(value = "spu编码")
    private String spuCode;
    @ApiModelProperty(value = "spu名称")
    private String spuName;
    @ApiModelProperty(value = "sku编号")
    private String skuCode;
    @ApiModelProperty(value = "sku名称")
    private String skuName;
    @ApiModelProperty(value = "品牌编码")
    private String brandCode;
    @ApiModelProperty(value = "品牌名称")
    private String brandName;
    @ApiModelProperty(value = "品类编码")
    private String categoryCode;
    @ApiModelProperty(value = "品类名称")
    private String categoryName;
    @ApiModelProperty(value = "规格")
    private String productSpec;
    @ApiModelProperty(value = "颜色编码")
    private String colorCode;
    @ApiModelProperty(value = "颜色名称")
    private String colorName;
    @ApiModelProperty(value = "型号")
    private String modelCode;
    @ApiModelProperty(value = "单位编码")
    private String unitCode;
    @ApiModelProperty(value = "单位名称")
    private String unitName;
    @ApiModelProperty(value = "商品类型   0商品 1赠品 2实物返回")
    private Boolean productType;
    @ApiModelProperty(value = "采购件数（整数）")
    private Long purchaseWhole;
    @ApiModelProperty(value = "采购件数（零数）")
    private Long purchaseSingle;
    @ApiModelProperty(value = "")
    private Long baseProductSpec;
    @ApiModelProperty(value = "采购包装")
    private String boxGauge;
    @ApiModelProperty(value = "税率")
    private BigDecimal taxRate;
    @ApiModelProperty(value = "行号")
    private Long lineCode;
    @ApiModelProperty(value = "厂商SKU编码")
    private String factorySkuCode;
    @ApiModelProperty(value = "商品含税单价")
    private BigDecimal taxAmount;
    @ApiModelProperty(value = "商品含税总价")
    private BigDecimal totalTaxAmount;
    @ApiModelProperty(value = "最小单位数量")
    private Long totalCount;
    @ApiModelProperty(value = "实际含税总价")
    private BigDecimal actualTotalTaxAmount;
    @ApiModelProperty(value = "0. 启用   1.禁用")
    private Boolean useStatus;
}