package com.aiqin.mgs.order.api.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 描述:
 *
 * @author zhujunchao
 * @create 2019-01-08 16:17
 */
@ApiModel("订单状态")
@Data
public class OrderStatus {
    private  String id;
    @ApiModelProperty(value = "订单状态code")
    @JsonProperty("order_status_code")
    private Integer orderStatusCode;

    @ApiModelProperty(value = "支付状态(0:未支付 1:已支付 2:已退款)")
    @JsonProperty("payment_status")
    private Integer paymentStatus ;

    @ApiModelProperty(value = "前台订单状态(显示)")
    @JsonProperty("reception_order_status")
    private Integer receptionOrderStatus;

    @ApiModelProperty(value = "后台订单状态(显示)")
    @JsonProperty("backstage_order_status")
    private Integer backstageOrderStatus;

    @ApiModelProperty(value = "说明")
    @JsonProperty("explain")
    private Integer explain;

    @ApiModelProperty(value = "标准描述")
    @JsonProperty("standard_description")
    private Integer standardDescription;
}
