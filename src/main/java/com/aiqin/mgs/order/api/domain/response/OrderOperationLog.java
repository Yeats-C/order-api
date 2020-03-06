package com.aiqin.mgs.order.api.domain.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@ApiModel
@Data
public class OrderOperationLog {
    @ApiModelProperty(value="")
    @JsonProperty("id")
    private Long id;

    @ApiModelProperty(value="来源id")
    @JsonProperty("operation_id")
    private String operationId;

    @ApiModelProperty(value="操作状态  : 0 新增 1 修改 2 下载")
    @JsonProperty("operation_type")
    private Integer operationType;

    @ApiModelProperty(value="操作内容")
    @JsonProperty("operation_content")
    private String operationContent;

    @ApiModelProperty(value="备注")
    @JsonProperty("remark")
    private String remark;

    @ApiModelProperty(value="")
    @JsonProperty("create_by_id")
    private String createById;

    @ApiModelProperty(value="")
    @JsonProperty("create_by_name")
    private String createByName;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value="")
    @JsonProperty("create_time")
    private Date createTime;

    @ApiModelProperty(value="")
    @JsonProperty("update_time")
    private Date updateTime;

    public OrderOperationLog() {
    }

    public OrderOperationLog(String operationId, Integer operationType, String operationContent, String remark, String createById, String createByName) {
        this.operationId = operationId;
        this.operationType = operationType;
        this.operationContent = operationContent;
        this.remark = remark;
        this.createById = createById;
        this.createByName = createByName;
    }
}