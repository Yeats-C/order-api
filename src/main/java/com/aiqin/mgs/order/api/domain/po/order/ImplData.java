package com.aiqin.mgs.order.api.domain.po.order;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ImplData {
    @ApiModelProperty(value = "订单明细行id")
    private String detailId;

    @ApiModelProperty(value = "入库数量")
    private String impQty;
}
