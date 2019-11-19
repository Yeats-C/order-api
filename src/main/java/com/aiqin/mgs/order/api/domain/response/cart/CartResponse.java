package com.aiqin.mgs.order.api.domain.response.cart;

import com.aiqin.mgs.order.api.domain.CartOrderInfo;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@ApiModel(value = "购物车响应参数，计算金额，数量")
public class CartResponse {

    private List<CartOrderInfo> cartInfoList;

    private BigDecimal acountActualprice;

    private int totalNumber;
}
