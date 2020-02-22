package com.aiqin.mgs.order.api.domain.response.cart;

import com.aiqin.mgs.order.api.domain.CartGroupInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value = "门店查询订单确认页面信息")
public class StoreCartProductResponse {

    @ApiModelProperty(value = "楼层列表 每一行是一个楼层")
    @JsonProperty("cart_group_list")
    private List<CartGroupInfo> cartGroupList;

    //商品原价汇总
    //商品活动价格汇总
    //A品扣除活动优惠后的金额汇总

//    @ApiModelProperty(value = "勾选商品原价汇总")
//    @JsonProperty("account_actual_price")
//    private BigDecimal accountActualPrice;
//
//    @ApiModelProperty(value = "勾选商品活动价汇总")
//    @JsonProperty("account_actual_activity_price")
//    private BigDecimal accountActualActivityPrice;
//
//    @ApiModelProperty(value = "商品总数量")
//    @JsonProperty("total_number")
//    private int totalNumber;
//
//    @ApiModelProperty(value = "所有勾选的商品中可使用A品券的商品活动后金额汇总")
//    @JsonProperty("top_total_price")
//    private BigDecimal topTotalPrice;

}
