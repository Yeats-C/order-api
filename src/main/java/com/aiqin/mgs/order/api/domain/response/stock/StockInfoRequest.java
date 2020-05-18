package com.aiqin.mgs.order.api.domain.response.stock;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author: zhao shuai
 * @create: 2020-04-08
 **/
@Data
public class StockInfoRequest {

    @ApiModelProperty("公司编码")
    @JsonProperty(value = "company_code")
    private String companyCode;

    @ApiModelProperty("公司名称")
    @JsonProperty(value = "company_name")
    private String companyName;

    @ApiModelProperty("仓库编码")
    @JsonProperty(value = "transport_center_code")
    private String transportCenterCode;

    @ApiModelProperty("仓库名称")
    @JsonProperty(value = "transport_center_name")
    private String transportCenterName;

    @ApiModelProperty("库房编码")
    @JsonProperty(value = "warehouse_code")
    private String warehouseCode;

    @ApiModelProperty("库房名称")
    @JsonProperty(value = "warehouse_name")
    private String warehouseName;

    @ApiModelProperty("仓库类型")
    @JsonProperty(value = "warehouse_type")
    private Integer warehouseType;

    @ApiModelProperty("sku编码")
    @JsonProperty(value = "sku_code")
    private String skuCode;

    @ApiModelProperty("sku名称")
    @JsonProperty(value = "sku_name")
    private String skuName;

    @ApiModelProperty("税率")
    @JsonProperty(value = "tax_rate")
    private BigDecimal taxRate;

    @ApiModelProperty("变化数")
    @JsonProperty(value = "change_count")
    private Long changeCount;

    @ApiModelProperty("最新供货单位编码")
    @JsonProperty(value = "new_delivery")
    private String newDelivery;

    @ApiModelProperty("最新供货单位名称")
    @JsonProperty(value = "new_delivery_name")
    private String newDeliveryName;

    @ApiModelProperty("最新采购价")
    @JsonProperty(value = "new_purchase_price")
    private BigDecimal newPurchasePrice;

    @ApiModelProperty("单据类型 0出库 1入库 2退供 3采购 4调拨 5退货 6移库 7监管仓入库 8报废 9订单 10监管仓出库 11损溢")
    @JsonProperty(value = "document_type")
    private Integer documentType;

    @ApiModelProperty("单据号")
    @JsonProperty(value = "document_code")
    private String documentCode;

    @ApiModelProperty("来源单据类型 0出库 1入库 2退供 3采购 4调拨 5退货 6移库 7监管仓入库 8报废 9订单 10监管仓出库 11损溢")
    @JsonProperty(value = "source_document_type")
    private Integer sourceDocumentType;

    @ApiModelProperty("来源单据号")
    @JsonProperty(value = "source_document_code")
    private String sourceDocumentCode;

    @ApiModelProperty("操作人")
    @JsonProperty(value = "operator_id")
    private String operatorId;

    @ApiModelProperty("操作人名称")
    @JsonProperty(value = "operator_name")
    private String operatorName;

    @ApiModelProperty("商品备注")
    @JsonProperty(value = "remark")
    private String remark;

    @ApiModelProperty("库存成本")
    @JsonProperty(value = "stock_cost")
    private BigDecimal stockCost;

    @ApiModelProperty("预计在途数")
    @JsonProperty(value = "pre_way_count")
    private Long preWayCount;
}
