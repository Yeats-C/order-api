package com.aiqin.mgs.order.api.domain.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ReturnOrder {

    @ApiModelProperty(value = "已退货数量 ------传 ")
    private Long quantityReturnedCount;

    @ApiModelProperty(value = "(申请退货数量)退货数量 ----传")
    private Long returnProductCount;

    @ApiModelProperty(value = "退货单号")
    private String returnOrderCode;
}
