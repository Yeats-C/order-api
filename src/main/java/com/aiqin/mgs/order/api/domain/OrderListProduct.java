package com.aiqin.mgs.order.api.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("订单商品从表")
@Data
public class OrderListProduct {
    private String id;

    @ApiModelProperty(value = "订单主表id")
    @JsonProperty("order_list_id")
    private String orderListId;

    @ApiModelProperty(value = "订单编号")
    @JsonProperty("order_code")
    private String orderCode;

    @ApiModelProperty(value = "行号")
    @JsonProperty("line_number")
    private Integer lineNumber;

    @ApiModelProperty(value = "商品sku码")
    @JsonProperty("sku_code")
    private String skuCode;

    @ApiModelProperty(value = "商品名称")
    @JsonProperty("sku_name")
    private String skuName;

    @ApiModelProperty(value = "商品成交价格")
    @JsonProperty("product_price")
    private Integer productPrice;


    @ApiModelProperty(value = "商品原本价格")
    @JsonProperty("original_product_price")
    private Integer originalProductPrice;


    @ApiModelProperty(value = "商品数量")
    @JsonProperty("product_number")
    private Integer productNumber;

    @ApiModelProperty(value = "参与活动编号")
    @JsonProperty("activity_code")
    private String activityCode;

    @ApiModelProperty(value = "参与活动名称")
    @JsonProperty("activity_name")
    private String activityName;

    @ApiModelProperty(value = "是否赠品(0:不是赠品 1:是赠品)")
    @JsonProperty("gift")
    private Integer gift;

    @ApiModelProperty(value = "发货单号")
    @JsonProperty("invoice_code")
    private String invoiceCode;

    @ApiModelProperty(value = "出仓code")
    @JsonProperty("out_warehouse_code")
    private String outWarehouseCode;

    @ApiModelProperty(value = "出仓仓库名称")
    @JsonProperty("out_warehouse_name")
    private String outWarehouseName;

    @ApiModelProperty(value = "规格")
    @JsonProperty("specifications")
    private String specifications;

    @ApiModelProperty(value = "单位")
    @JsonProperty("unit")
    private String unit;

    @ApiModelProperty(value = "颜色code")
    @JsonProperty("color_code")
    private String colorCode;

    @ApiModelProperty(value = "颜色名称")
    @JsonProperty("color_name")
    private String colorName;


    @ApiModelProperty(value = "型号")
    @JsonProperty("model_number")
    private String modelNumber;
}