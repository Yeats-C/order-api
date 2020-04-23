package com.aiqin.mgs.order.api.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@ApiModel("首单赠送超额审批")
public class OrderGiveApproval {

    @ApiModelProperty(value = "id")
    @JsonProperty("id")
    private Long id;

    @ApiModelProperty(value = "审批编码")
    @JsonProperty("form_no")
    private String formNo;

    @ApiModelProperty(value = "加盟商名称")
    @JsonProperty("fanchisee_name")
    private String fanchiseeName;

    @ApiModelProperty(value = "加盟商id")
    @JsonProperty("fanchisee_id")
    private String fanchiseeId;

    @ApiModelProperty(value = "门店id")
    @JsonProperty("store_id")
    private String storeId;

    @ApiModelProperty(value = "门店名称")
    @JsonProperty("store_name")
    private String storeName;

    @ApiModelProperty(value = "首单赠送市值")
    @JsonProperty("market_value")
    private BigDecimal marketValue;

    @ApiModelProperty(value = "实际赠送市值")
    @JsonProperty("actual_market_value")
    private BigDecimal actualMarketValue;

    @ApiModelProperty(value = "申请时间")
    @JsonProperty("create_time")
    private Date createTime;

    @ApiModelProperty(value = "审批状态 0:待审批 1:审批完成")
    @JsonProperty("status")
    private Integer status;

    @ApiModelProperty(value = "审批名称")
    @JsonProperty("process_title")
    private String processTitle;

    @ApiModelProperty(value = "审批类型")
    @JsonProperty("process_type")
    private String processType;

    @ApiModelProperty(value = "审批状态描述")
    @JsonProperty("statu_str")
    private String statuStr;

    @ApiModelProperty(value = "发起人姓名")
    @JsonProperty("applier_name")
    private String applierName;

    @ApiModelProperty(value = "发起人ID")
    @JsonProperty("applier_id")
    private String applierId;

    @ApiModelProperty(value = "操作人")
    @JsonProperty("pre_node_opt_user")
    private String preNodeOptUser;

    @ApiModelProperty(value = "操作时间")
    @JsonProperty("pre_node_time")
    private Date preNodeTime;

    @ApiModelProperty(value = "订单编码")
    @JsonProperty("order_code")
    private String orderCode;

}