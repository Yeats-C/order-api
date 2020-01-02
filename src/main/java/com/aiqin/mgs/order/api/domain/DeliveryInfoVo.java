package com.aiqin.mgs.order.api.domain;


import com.fasterxml.jackson.annotation.JsonFormat;
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
    @JsonProperty("delivery_time")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date deliveryTime;

    @ApiModelProperty(value = "发运人")
    @JsonProperty("customer_person")
    private String customerPerson;

    @ApiModelProperty(value = "发运单明细")
    @JsonProperty("delivery_detail")
    private List<DeliveryDetailInfo> deliveryDetail;

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
    private String transportCode;

    @ApiModelProperty(value = "发运状态")
    @JsonProperty("transport_status")
    private Integer transportStatus;

    @ApiModelProperty(value = "发货仓库名称")
    @JsonProperty("transport_center_name")
    private String transportCenterName;

    @ApiModelProperty(value = "发货仓库编码")
    @JsonProperty("transport_center_code")
    private String transportCenterCode;
}
