package com.aiqin.mgs.order.api.domain.request.order;

import com.fasterxml.jackson.annotation.JsonProperty;
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

    @ApiModelProperty(value = "spu编码，商品编码")
    @JsonProperty("spu_code")
    private String spuCode;

    @ApiModelProperty(value = "sku编码")
    @JsonProperty("sku_code")
    private String skuCode;

    @ApiModelProperty(value = "数量")
    @JsonProperty("quantity")
    private Integer quantity;

    @ApiModelProperty(value = "单价 创建货架订单时使用")
    @JsonProperty("price")
    private BigDecimal price;

    @ApiModelProperty(value = "含税采购价 创建货架订单时使用")
    @JsonProperty("tax_price")
    private BigDecimal taxPrice;

    @ApiModelProperty(value = "行号 签收时使用")
    @JsonProperty("line_code")
    private Long lineCode;

    @ApiModelProperty(value = "门店实收数量 签收时使用")
    @JsonProperty("actual_inbound_count")
    private Long actualInboundCount;

    @ApiModelProperty(value = "签收数量差异原因 签收时使用")
    @JsonProperty("sign_difference_reason")
    private String signDifferenceReason;

}
