package com.aiqin.mgs.order.api.domain.po.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 订单操作日志实体
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/12/9 9:58
 */
@Data
public class ErpOrderOperationLog {

    @ApiModelProperty(value = "主键")
    @JsonProperty("id")
    private Long id;

    @ApiModelProperty(value = "来源编码")
    @JsonProperty("operation_code")
    private String operationCode;

    @ApiModelProperty(value = "日志类型 0 .新增 1.修改 2.删除 3.下载")
    @JsonProperty("operation_type")
    private Integer operationType;

    @ApiModelProperty(value = "来源类型 0.销售 1.采购 2.退货  3.退供")
    @JsonProperty("source_type")
    private Integer sourceType;

    @ApiModelProperty(value = "关联单据状态")
    @JsonProperty("operation_status")
    private Integer operationStatus;

    @ApiModelProperty(value = "日志内容")
    @JsonProperty("operation_content")
    private String operationContent;

    @ApiModelProperty(value = "备注")
    @JsonProperty("remark")
    private String remark;

    @ApiModelProperty(value = "启用状态 0.启用 1.禁用")
    @JsonProperty("use_status")
    private Integer useStatus;

    @ApiModelProperty(value = "创建时间")
    @JsonProperty("create_time")
    private Date createTime;

    @ApiModelProperty(value = "创建人id")
    @JsonProperty("create_by_id")
    private String createById;

    @ApiModelProperty(value = "创建人姓名")
    @JsonProperty("create_by_name")
    private String createByName;

    @ApiModelProperty(value = "更新时间")
    @JsonProperty("update_time")
    private Date updateTime;

    @ApiModelProperty(value = "修改人id")
    @JsonProperty("update_by_id")
    private String updateById;

    @ApiModelProperty(value = "修改人姓名")
    @JsonProperty("update_by_name")
    private String updateByName;

}
