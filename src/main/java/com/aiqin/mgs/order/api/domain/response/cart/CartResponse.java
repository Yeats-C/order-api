package com.aiqin.mgs.order.api.domain.response.cart;

import com.aiqin.mgs.order.api.domain.CartOrderInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@ApiModel(value = "购物车响应参数，计算金额，数量")
public class CartResponse {

    @ApiModelProperty(value = "商品列表")
    @JsonProperty("cartInfoList")
    private List<CartOrderInfo> cartInfoList;

    @ApiModelProperty(value = "勾选商品原价汇总")
    @JsonProperty("account_actual_price")
    private BigDecimal accountActualPrice;

    @ApiModelProperty(value = "商品总数量")
    @JsonProperty("total_number")
    private int totalNumber;
}
