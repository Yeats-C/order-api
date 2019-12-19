package com.aiqin.mgs.order.api.domain.request.order;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 订单商品行数据
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/12/17 21:00
 */
@Data
public class ErpOrderProductItemRequest {

    @ApiModelProperty(value = "spu编码")
    private String spuCode;
    @ApiModelProperty(value = "sku编码")
    private String skuCode;
    @ApiModelProperty(value = "数量")
    private Integer quantity;
    @ApiModelProperty(value = "单价")
    private BigDecimal price;
    @ApiModelProperty(value = "含税采购价")
    private BigDecimal taxPrice;

}
