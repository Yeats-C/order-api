package com.aiqin.mgs.order.api.domain.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ReturnOrderDetailList {

    @ApiModelProperty(value = "退货单号")
    private String returnOrderCode;

    @ApiModelProperty(value = "sku编号")
    private String skuCode;

    @ApiModelProperty(value = "sku名称")
    private String skuName;

    @ApiModelProperty(value = "客户实收数量")
    private Long actualInboundCount;

    @ApiModelProperty(value = "已退货数量 ------传 ")
    private Long quantityReturnedCount;

    @ApiModelProperty(value = "(申请退货数量)退货数量 ----传")
    private Long returnProductCount;
}
