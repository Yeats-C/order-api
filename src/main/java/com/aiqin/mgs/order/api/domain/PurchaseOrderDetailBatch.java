package com.aiqin.mgs.order.api.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;
/**
 * 采购商品批次
 * TableName: purchase_order_detail_batch
 */
@Data
@ToString(callSuper = true)
@ApiModel("采购商品批次")
public class PurchaseOrderDetailBatch implements Serializable {
    private String purchaseOrderDetailBatchId;
    @ApiModelProperty(value = "采购单号")
    private String purchaseOrderCode;
    @ApiModelProperty(value = "sku编号")
    private String skuCode;
    @ApiModelProperty(value = "sku名称")
    private String skuName;
    @ApiModelProperty(value = "批次号")
    private String batchCode;
    @ApiModelProperty(value = "生产日期",example = "2001-01-01 01:01:01")
    private Date productDate;
    @ApiModelProperty(value = "批次备注")
    private String batchRemark;
    @ApiModelProperty(value = "单位编码")
    private String unitCode;
    @ApiModelProperty(value = "单位名称")
    private String unitName;
    @ApiModelProperty(value = "采购件数（整数）")
    private Long purchaseWhole;
    @ApiModelProperty(value = "采购件数（零数）")
    private Long purchaseSingle;
    @ApiModelProperty(value = "基商品含量")
    private Long baseProductSpec;
    @ApiModelProperty(value = "最小单位数量")
    private Long totalCount;
    @ApiModelProperty(value = "实际最小单位数量")
    private Long actualTotalCount;
    @ApiModelProperty(value = "行号")
    private Long lineCode;
    @ApiModelProperty(value = "0. 启用   1.禁用")
    private Boolean useStatus;
    @ApiModelProperty(value = "创建人编码")
    private String createById;
    @ApiModelProperty(value = "创建人名称")
    private String createByName;
    @ApiModelProperty(value = "修改人编码")
    private String updateById;
    @ApiModelProperty(value = "修改人名称")
    private String updateByName;
    @ApiModelProperty(value = "创建时间",example = "2001-01-01 01:01:01")
    private Date createTime;
    @ApiModelProperty(value = "修改时间",example = "2001-01-01 01:01:01")
    private Date updateTime;
}