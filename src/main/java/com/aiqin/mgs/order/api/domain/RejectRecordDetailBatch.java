package com.aiqin.mgs.order.api.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

/**
 * 退供商品批次信息
 * TableName reject_record_detail_batch
 */
@Data
@ApiModel("退供商品批次信息")
@ToString(callSuper = true)
public class RejectRecordDetailBatch {
    @ApiModelProperty(value = "id")
    @JsonProperty("id")
    private String id;
    @ApiModelProperty(value = "业务id")
    private String rejectRecordDetailBatchId;
    @ApiModelProperty(value = "退供单号")
    private String rejectRecordCode;
    @ApiModelProperty(value = "sku编号")
    private String skuCode;
    @ApiModelProperty(value = "sku名称")
    private String skuName;
    @ApiModelProperty(value = "batch_code")
    private String batchCode;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date productDate;
    @ApiModelProperty(value = "批次备注")
    private String batchRemark;
    @ApiModelProperty(value = "单位编码")
    private String unitCode;
    @ApiModelProperty(value = "单位名称")
    private String unitName;
    @ApiModelProperty(value = "商品数量")
    private Long productCount;
    @ApiModelProperty(value = "最小单位数量")
    private Long totalCount;
    @ApiModelProperty(value = "实际最小单数数量")
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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;
}