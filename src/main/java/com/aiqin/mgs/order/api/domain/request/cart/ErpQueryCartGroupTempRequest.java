package com.aiqin.mgs.order.api.domain.request.cart;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
@ApiModel("订单结算数据缓存关联查询参数")
public class ErpQueryCartGroupTempRequest {

    @ApiModelProperty(value = "订单结算缓存数据关联key")
    @JsonProperty("cart_group_temp_key")
    @NotEmpty(message = "key不能为空")
    private String cartGroupTempKey;

}
