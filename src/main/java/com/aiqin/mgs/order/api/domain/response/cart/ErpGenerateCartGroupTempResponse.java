package com.aiqin.mgs.order.api.domain.response.cart;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "生成订单结算数据返回值")
public class ErpGenerateCartGroupTempResponse {

    @ApiModelProperty(value = "订单结算缓存数据关联key")
    @JsonProperty("cart_group_temp_key")
    private String cartGroupTempKey;

}
