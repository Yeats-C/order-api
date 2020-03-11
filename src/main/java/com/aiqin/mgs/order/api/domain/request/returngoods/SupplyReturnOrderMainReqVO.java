package com.aiqin.mgs.order.api.domain.request.returngoods;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * Description:
 *
 * @author: zth
 * @date: 2018-12-30
 * @time: 17:01
 */
@Data
@ApiModel("退货订单信息")
public class SupplyReturnOrderMainReqVO {
    @ApiModelProperty("退货订单信息")
    private SupplyReturnOrderInfoReqVO mainOrderInfo;

    @ApiModelProperty("商品信息")
    private List<SupplyReturnOrderProductItemReqVO> orderItems;
}
