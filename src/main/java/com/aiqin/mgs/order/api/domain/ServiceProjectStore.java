package com.aiqin.mgs.order.api.domain;

import com.aiqin.mgs.order.api.base.PagesRequest;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import java.util.Date;


@Data
@ApiModel("门店服务商品")
@JsonInclude(JsonInclude.Include.ALWAYS)
public class ServiceProjectStore extends PagesRequest {

    @ApiModelProperty(value = "自增主键")
    @JsonProperty("id")
    private Long id;

    @ApiModelProperty(value = "服务项目id")
    @JsonProperty("project_id")
    @NotBlank
    private String projectId;

    @ApiModelProperty(value = "服务项目编号")
    @JsonProperty("project_code")
    private String projectCode;

    @ApiModelProperty(value = "服务项目名称")
    @JsonProperty("project_name")
    private String projectName;

    @ApiModelProperty(value = "服务类别id")
    @JsonProperty("type_id")
    private String typeId;

    @ApiModelProperty(value = "门店id")
    @JsonProperty("store_id")
    @NotBlank
    private String storeId;

    @ApiModelProperty(value = "门店编号")
    @JsonProperty("store_code")
    private String storeCode;

    @ApiModelProperty(value = "门店名称")
    @JsonProperty("store_name")
    private String storeName;

    @ApiModelProperty(value = "消费方式，0为限次，1为不限次")
    @JsonProperty("consumption_pattern")
    private Integer consumptionPattern;

    @ApiModelProperty(value = "限制次数")
    @JsonProperty("limit_count")
    private Integer limitCount;

    @ApiModelProperty(value = "实际金额")
    @JsonProperty("actual_amount")
    private Long actualAmount;

    @ApiModelProperty(value = "有效期")
    @JsonProperty("effective_time")
    private Integer effectiveTime;

    @ApiModelProperty(value = "有效期单位")
    @JsonProperty("unit_no")
    private Integer unitNo;

    @ApiModelProperty(value = "积分系数，备用")
    @JsonProperty("point_coefficient")
    private Integer pointCoefficient;

    @ApiModelProperty(value = "备注")
    @JsonProperty("remark")
    private  String remark;

    @ApiModelProperty(value = "状态，0为在用，1为停用")
    @JsonProperty("use_status")
    private Integer useStatus;

    @ApiModelProperty(value = "是否删除，0为未删除，1为删除")
    @JsonProperty("delete_flag")
    private Integer deleteFlag;

    @ApiModelProperty(value = "创建时间")
    @JsonProperty("create_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    @JsonProperty("update_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    @ApiModelProperty(value = "创建人")
    @JsonProperty("create_by")
    private String createBy;

    @ApiModelProperty(value = "更新人")
    @JsonProperty("update_by")
    private String updateBy;

    @ApiModelProperty(value = "总销售量")
    @JsonProperty("total_count")
    private Integer totalCount;
}
