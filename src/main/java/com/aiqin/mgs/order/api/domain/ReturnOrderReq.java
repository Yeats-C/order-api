package com.aiqin.mgs.order.api.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
@Data
public class ReturnOrderReq implements Serializable {
    @ApiModelProperty(value = "退供单")
    private ReturnOrderInfo returnOrderInfo;
    @ApiModelProperty(value = "退供单明细")
    private List<ReturnOrderDetail> returnOrderDetails;
}
