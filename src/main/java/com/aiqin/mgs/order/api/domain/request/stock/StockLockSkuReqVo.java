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

    @ApiModelProperty("商品行号")
    @JsonProperty("line_num")
    private String lineNum;

    @ApiModelProperty("sku编码")
    @JsonProperty("sku_code")
    private String skuCode;

    @ApiModelProperty("订单数量")
    private Integer num;

    @JsonProperty("product_type")
    @ApiModelProperty("商品类型，1-正常品，0-效期品")
    private Integer productType = 1;
}
