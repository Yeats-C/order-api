package com.aiqin.mgs.order.api.domain.response.cart;

import com.aiqin.mgs.order.api.domain.CartOrderInfo;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CartResponse {

    private List<CartOrderInfo> cartInfoList;

    private BigDecimal acountActualprice;

    private int totalNumber;
}
