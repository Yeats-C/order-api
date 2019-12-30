package com.aiqin.mgs.order.api.domain;

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
public class OperationLog {
    @ApiModelProperty(value = "来源编码")
    private String operationCode;
    @ApiModelProperty(value = "日志类型 0 .新增 1.修改 2.删除 3.下载 ")
    private Byte operationType;
    @ApiModelProperty(value = "来源类型 0.销售 1.采购 2.退货  3.退供")
    private Integer sourceType;
    @ApiModelProperty(value = "日志内容")
    private String operationContent;
    @ApiModelProperty(value = "备注")
    private String remark;
    @ApiModelProperty(value = "0. 启用 1.禁用")
    private String useStatus;
    @ApiModelProperty(value = "创建人编码")
    private String createById;
    @ApiModelProperty(value = "创建人名称")
    private String createByName;
    @ApiModelProperty(value = "修改人编码")
    private String updateById;
    @ApiModelProperty(value = "修改人名称")
    private String updateByName;
    @ApiModelProperty(value = "创建时间")
    private String createTime;
    @ApiModelProperty(value = "修改时间")
    private String updateTime;
}