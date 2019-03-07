package com.aiqin.mgs.order.api.domain.request.orderList;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 描述:
 *
 * @author zhujunchao
 * @create 2019-03-07 15:38
 */
@ApiModel("添加商品实发数量")
@Data
public class ActualDeliverVo {
    @ApiModelProperty(value = "订单商品ID")
    @JsonProperty("order_product_id")
    private String orderProductId;
    @ApiModelProperty(value = "实发数量")
    @JsonProperty("actual_deliver_num")
    private Integer actualDeliverNum;

}
