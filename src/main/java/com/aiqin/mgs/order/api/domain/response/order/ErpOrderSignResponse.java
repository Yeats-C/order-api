package com.aiqin.mgs.order.api.domain.response.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 查询门店未签收订单数量查询返回值
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/12/16 11:20
 */
@Data
public class ErpOrderSignResponse {

    @ApiModelProperty(value = "门店未签收订单数量")
    @JsonProperty("need_sign_order_quantity")
    private Integer needSignOrderQuantity;
}
