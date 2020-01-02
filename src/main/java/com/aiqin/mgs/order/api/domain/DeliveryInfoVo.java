package com.aiqin.mgs.order.api.domain;


import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class DeliveryInfoVo implements Serializable {
    //发运信息
    @ApiModelProperty(value = "发运单编号")
    private String deliveryCode;
    @ApiModelProperty(value = "发运时间")
    private Date customerTime;
    @ApiModelProperty(value = "配送方式编码")
    private String distributionModeCode;
    @ApiModelProperty(value = "配送方式名称")
    private String distributionModeName;
    @ApiModelProperty(value = "发运人id")
    private String customerPersonId;
    @ApiModelProperty(value = "发运人id")
    private String customerPersonName;
    @ApiModelProperty(value = "发运单明细")
    private DeliverydetailInfo deliverydetailInfo;


    @ApiModelProperty(value = "客户编号")
    private String customerCode;
    @ApiModelProperty(value = "客户名称")
    private String customerName;
    @ApiModelProperty(value = "发运单运费")
    private BigDecimal transportAmount;
    @ApiModelProperty(value = "物流公司")
    private String transportCompanyCode;
    @ApiModelProperty(value = "物流公司")
    private String transportCompanyName;
    @ApiModelProperty(value = "物流单号")
    private List<String> transportCode;
    @ApiModelProperty(value = "发运状态")
    private Integer transportStatus;
    @ApiModelProperty(value = "仓库名称")
    private String sendRepertoryName;
    @ApiModelProperty(value = "仓库编码")
    private String sendRepertoryCode;
    @ApiModelProperty(value = "物流费用")
    private BigDecimal logisticsFee;
}
