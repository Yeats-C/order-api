package com.aiqin.mgs.order.api.domain.response.cart;

import com.aiqin.mgs.order.api.domain.po.cart.ErpOrderCartInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@ApiModel(value = "erp端查询购物车返回数据")
public class ErpCartQueryResponse {

    @ApiModelProperty(value = "商品列表")
    @JsonProperty("cartInfoList")
    private List<ErpOrderCartInfo> cartInfoList;

    @ApiModelProperty(value = "商品价格汇总")
    @JsonProperty("account_actual_price")
    private BigDecimal accountActualPrice;

    @ApiModelProperty(value = "商品数量汇总")
    @JsonProperty("total_number")
    private Integer totalNumber;
}
