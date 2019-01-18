package com.aiqin.mgs.order.api.domain.request.orderList;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * Description:
 * 订单信息
 * @author: zth
 * @date: 2018-12-25
 * @time: 11:15
 */
@Data
@ApiModel("订单信息")
public class SupplyOrderMainReqVO {
    @ApiModelProperty("父订单信息")
    private SupplyOrderInfoReqVO mainOrderInfo;

    @ApiModelProperty("子订单信息")
    private List<SupplyOrderInfoReqVO> subOrderInfo;

}
