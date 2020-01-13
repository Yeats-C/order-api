package com.aiqin.mgs.order.api.domain.request.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 商品出货请求商品行明细
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2020/1/1 15:46
 */
@Data
public class ErpOrderDeliverItemRequest {

    @ApiModelProperty(value = "行号")
    @JsonProperty("line_code")
    private Long lineCode;

    @ApiModelProperty(value = "实发数量")
    @JsonProperty("actual_product_count")
    private Long actualProductCount;

}
