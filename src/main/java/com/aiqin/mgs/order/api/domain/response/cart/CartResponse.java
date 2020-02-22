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

    @ApiModelProperty(value = "勾选商品活动价汇总")
    @JsonProperty("account_actual_activity_price")
    private BigDecimal accountActualActivityPrice;

    @ApiModelProperty(value = "商品总数量")
    @JsonProperty("total_number")
    private int totalNumber;

    @ApiModelProperty(value = "所有勾选的商品中可使用A品券的商品活动后金额汇总")
    @JsonProperty("top_total_price")
    private BigDecimal topTotalPrice;
}
