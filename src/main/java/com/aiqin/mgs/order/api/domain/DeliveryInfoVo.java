package com.aiqin.mgs.order.api.domain;


import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class DeliveryInfoVo implements Serializable {
    //发运信息
    @ApiModelProperty(value = "发运单编号")
    @JsonProperty("delivery_code")
    @NotEmpty(message = "发运单编号不能为空")
    private String deliveryCode;

    @ApiModelProperty(value = "发运时间")
    @JsonProperty("customer_time")
    private Date customerTime;
    @ApiModelProperty(value = "配送方式编码")
    @JsonProperty("distribution_mode_code")
    private String distributionModeCode;

    @ApiModelProperty(value = "配送方式名称")
    @JsonProperty("distribution_mode_name")
    private String distributionModeName;

    @ApiModelProperty(value = "发运人id")
    @JsonProperty("customer_person_id")
    private String customerPersonId;

    @ApiModelProperty(value = "发运人id")
    @JsonProperty("customer_person_name")
    private String customerPersonName;

    @ApiModelProperty(value = "发运单明细")
    @JsonProperty("deliverydetail_infos")
    private List<DeliveryDetailInfo> deliveryDetailInfos;

    @ApiModelProperty(value = "客户编号")
    @JsonProperty("customer_code")
    private String customerCode;

    @ApiModelProperty(value = "客户名称")
    @JsonProperty("customer_name")
    private String customerName;

    @ApiModelProperty(value = "发运单运费")
    @JsonProperty("transport_amount")
    private BigDecimal transportAmount;

    @ApiModelProperty(value = "物流公司")
    @JsonProperty("transport_company_code")
    private String transportCompanyCode;

    @ApiModelProperty(value = "物流公司")
    @JsonProperty("transport_company_name")
    private String transportCompanyName;

    @ApiModelProperty(value = "物流单号")
    @JsonProperty("transport_code")
    @NotEmpty(message = "物流单号不能为空")
    private List<String> transportCode;

    @ApiModelProperty(value = "发运状态")
    @JsonProperty("transport_status")
    private Integer transportStatus;

    @ApiModelProperty(value = "仓库名称")
    @JsonProperty("send_repertory_name")
    private String sendRepertoryName;

    @ApiModelProperty(value = "仓库编码")
    @JsonProperty("send_repertory_code")
    private String sendRepertoryCode;

    @ApiModelProperty(value = "物流费用")
    @JsonProperty("logistics_fee")
    private BigDecimal logisticsFee;
}
