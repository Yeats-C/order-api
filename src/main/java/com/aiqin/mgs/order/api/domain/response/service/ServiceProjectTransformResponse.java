package com.aiqin.mgs.order.api.domain.response.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("服务项目的转化情况")
public class ServiceProjectTransformResponse {

    @ApiModelProperty(value = "总销售金额或者某个类别的总销售金额")
    @JsonProperty("total_amount")
    private Long totalAmount;

    @ApiModelProperty(value = "总退货金额或者某个类别的总退货金额")
    @JsonProperty("total_return_amount")
    private Long totalReturnAmount;

    @ApiModelProperty(value = "昨日销售金额")
    @JsonProperty("yesterday_amount")
    private Long yesterdayAmount;

    @ApiModelProperty(value = "总销售量或某个类别的总销量")
    @JsonProperty("total_count")
    private Integer totalCount;

    @ApiModelProperty(value = "总退货量或某个类别的总退货量")
    @JsonProperty("total_return_count")
    private Integer totalReturnCount;

    @ApiModelProperty(value = "昨日销量")
    @JsonProperty("yesterday_count")
    private Integer yesterdayCount;

    @ApiModelProperty(value = "总客流量或当月客流量")
    @JsonProperty("total_customer_count")
    private Integer totalCustomerCount;

    @ApiModelProperty(value = "昨日客流量")
    @JsonProperty("yesterday_customer_count")
    private Integer yesterdayCustomerCount;

    @ApiModelProperty(value = "在用商品数")
    @JsonProperty("use_project_count")
    private Integer useProjectCount;

    @ApiModelProperty(value = "服务类别id")
    @JsonProperty("type_id")
    private String typeId;

    @ApiModelProperty(value = "服务类别名称")
    @JsonProperty("type_name")
    private String typeName;

    @ApiModelProperty(value = "服务商品id")
    @JsonProperty("project_id")
    private String projectId;

    @ApiModelProperty(value = "服务商品名称")
    @JsonProperty("project_name")
    private String projectName;

}
