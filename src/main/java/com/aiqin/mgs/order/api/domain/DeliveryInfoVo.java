package com.aiqin.mgs.order.api.domain;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
@Data
public class DeliveryInfoVo implements Serializable {
    @ApiModelProperty(value = "发运单编号")
    private String deliveryCode;
    @ApiModelProperty(value = "客户编号")
    private String customerCode;
    @ApiModelProperty(value = "客户名称")
    private String customerName;
    @ApiModelProperty(value = "发运时间")
    private Date customerTime;
    @ApiModelProperty(value = "发运人id")
    private String customerPersonId;
    @ApiModelProperty(value = "发运单运费")
    private BigDecimal transportAmount;
    @ApiModelProperty(value = "物流公司")
    private String transportCompanyCode;
    @ApiModelProperty(value = "物流单号")
    private String transportCode;
    @ApiModelProperty(value = "发运单明细")
    private DeliverydetailInfo deliverydetailInfo;
}
