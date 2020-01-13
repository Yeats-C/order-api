package com.aiqin.mgs.order.api.domain.request.bill;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Auther: mamingze
 * @Date: 2020-01-03 18:11
 * @Description:
 */
@Data
public class ReturnDLReq {
    @ApiModelProperty(value = "DL退货主表")
    private ReturnOrderInfoDLReq returnOrderInfoDLReq;
    @ApiModelProperty(value = "退货单明细")
    private List<ReturnOrderDetailDLReq> returnOrderDetailDLReqList;

    @ApiModelProperty(value = "退货单批次明细")
    private List<ReturnBatchDetailDLReq> returnBatchDetailDLReqList;
}
