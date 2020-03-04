package com.aiqin.mgs.order.api.domain.response.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;


@ApiModel("订单批次信息")
@Data
public class QueryOrderInfoItemBatchRespVO {

    @ApiModelProperty("订单编码")
    private String orderCode;

    @ApiModelProperty("商品行号")
    private Long productLineNum;

    @ApiModelProperty("sku编号")
    private String skuCode;

    @ApiModelProperty("sku名称")
    private String skuName;

    @ApiModelProperty("数量")
    private Long num;

    @ApiModelProperty("实发数量")
    private Long actualDeliverNum;

    @ApiModelProperty("生产日期")
    private Date productTime;

    @ApiModelProperty("批次备注")
    private String batchRemark;

    @ApiModelProperty("批次号")
    private String batchNumber;

    @ApiModelProperty("锁定类型")
    private Integer lockType;

    @ApiModelProperty("供应商编码")
    private String supplierCode;

    @ApiModelProperty("供应商名称")
    private String supplierName;
}
