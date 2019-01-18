package com.aiqin.mgs.order.api.domain.request.stock;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * StockLockLockReqVo
 *
 * @author zhangtao
 * @createTime 2019-01-18
 * @description
 */
@Data
public class StockLockSkuReqVo {

    @ApiModelProperty("sku编码")
    @JsonProperty("sku_code")
    private String sku_code;

    @ApiModelProperty("订单数量")
    private Integer num;
}
