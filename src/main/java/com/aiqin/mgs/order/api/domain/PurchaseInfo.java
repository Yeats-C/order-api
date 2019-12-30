package com.aiqin.mgs.order.api.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class PurchaseInfo implements Serializable {
    //订单主表
    @ApiModelProperty(value = "销售单单号")
    private String orderCode;
    @ApiModelProperty(value = "实际销售数量")
    private Long actualTotalCount;
    @ApiModelProperty(value = "发货时间")
    private Date deliveryTime;
    @ApiModelProperty(value = "发货人id")
    private String deliveryPersionId;
    //订单明细
    @ApiModelProperty(value = "订单明细")
    private List<PurchaseDetailInfo> purchaseDetailInfo;
    //订单批次明细
    @ApiModelProperty(value = "订单批次明细")
    private  PurchaseBanchInfo PurchaseBanchInfo;
}
