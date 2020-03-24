package com.aiqin.mgs.order.api.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

/**
 * 日志信息
 * TableName: operation_log
 */
@Data
@ToString(callSuper = true)
@ApiModel("日志信息")
public class OperationOrderLog {

    @ApiModelProperty(value = "来源编码")
    @JsonProperty("operation_code")
    private String operationCode;

    @ApiModelProperty(value = "日志类型 0 .新增 1.修改 2.删除 3.下载 ")
    @JsonProperty("operation_type")
    private Integer operationType;

    @ApiModelProperty(value = "来源类型 0.销售 1.采购 2.退货  3.退供")
    @JsonProperty("source_type")
    private Integer sourceType;

    @ApiModelProperty(value = "日志内容")
    @JsonProperty("operation_content")
    private String operationContent;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "0. 启用 1.禁用")
    @JsonProperty("use_status")
    private String useStatus;

    @ApiModelProperty(value = "创建人编码")
    @JsonProperty("create_by_id")
    private String createById;

    @ApiModelProperty(value = "创建人名称")
    @JsonProperty("create_by_name")
    private String createByName;

    @ApiModelProperty(value = "修改人编码")
    @JsonProperty("update_by_id")
    private String updateById;

    @ApiModelProperty(value = "修改人名称")
    @JsonProperty("update_by_name")
    private String updateByName;

    @ApiModelProperty(value = "创建时间")
    @JsonProperty("create_time")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private String createTime;

    @ApiModelProperty(value = "修改时间")
    @JsonProperty("update_time")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private String updateTime;
}