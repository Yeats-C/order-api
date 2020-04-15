package com.aiqin.mgs.order.api.domain.po.gift;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@ApiModel(value = "erp端查询购物车返回数据")
public class GiftCartQueryResponse {

    @ApiModelProperty(value = "兑换赠品池商品列表")
    @JsonProperty("gift_pool_list")
    private List<GiftPool> giftPoolList;

    @ApiModelProperty(value = "词门店兑换赠品已加入购物车价格汇总")
    @JsonProperty("account_actual_price")
    private BigDecimal accountActualPrice;

    @ApiModelProperty(value = "兑换赠品池商品数量汇总")
    @JsonProperty("total_number")
    private Integer totalNumber;
}
