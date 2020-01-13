package com.aiqin.mgs.order.api.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
@Data
public class ReturnOrderDetailBatch {
    @ApiModelProperty(value = "主键id")
    @JsonProperty("id")
    private Long id;
    @ApiModelProperty(value = "业务id")
    @JsonProperty("return_order_detail_batch_id")
    private String returnOrderDetailBatchId;
    @ApiModelProperty(value = "退货单号")
    @JsonProperty("return_order_code")
    private String returnOrderCode;
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
    @ApiModelProperty(value = "创建时间",example = "2001-01-01 01:01:01")
    private Date createTime;
    @ApiModelProperty(value = "修改时间",example = "2001-01-01 01:01:01")
    private Date updateTime;
}