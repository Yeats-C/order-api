package com.aiqin.mgs.order.api.domain.request.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 订单编辑请求参数
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/12/9 11:52
 */
@Data
public class ErpOrderEditRequest {

    @ApiModelProperty(value = "订单编号")
    @JsonProperty("order_code")
    private String orderCode;

    @ApiModelProperty(value = "赠品行")
    @JsonProperty("product_gift_list")
    private List<ErpOrderProductItemRequest> productGiftList;
}
