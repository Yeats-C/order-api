package com.aiqin.mgs.order.api.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
@Data
public class OrderStoreDetailBatch {
    @ApiModelProperty(value = "业务id")
    private String orderStoreDetailBatchId;
    @ApiModelProperty(value = "订单号")
    private String orderStoreCode;
    @ApiModelProperty(value = "sku编号")
    private String skuCode;
    @ApiModelProperty(value = "sku名称")
    private String skuName;
    @ApiModelProperty(value = "批次号")
    private String batchCode;
    @ApiModelProperty(value = "生产日期")
    private Date productDate;
    @ApiModelProperty(value = "批次备注")
    private String batchRemark;
    @ApiModelProperty(value = "单位编码")
    private String unitCode;
    @ApiModelProperty(value = "单位名称")
    private String unitName;
    @ApiModelProperty(value = "数量")
    private Long productCount;
    @ApiModelProperty(value = "实际发货数量")
    private Long actualProductCount;
    @ApiModelProperty(value = "行号")
    private Long lineCode;
    @ApiModelProperty(value = "0. 启用   1.禁用")
    private Integer useStatus;
    @ApiModelProperty(value = "创建人编码")
    private String createById;
    @ApiModelProperty(value = "创建人名称")
    private String createByName;
    @ApiModelProperty(value = "修改人编码")
    private String updateById;
    @ApiModelProperty(value = "修改人名称")
    private String updateByName;
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
    @ApiModelProperty(value = "修改时间")
    private Date updateTime;
}