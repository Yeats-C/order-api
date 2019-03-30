package com.aiqin.mgs.order.api.domain.request.orderList;

import com.aiqin.mgs.order.api.base.PagesRequest;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 描述:
 *
 * @author zhujunchao
 * @create 2019-01-08 11:02
 */
@ApiModel("订单前台查询vo")
@Data
public class OrderListVo2 extends PagesRequest {
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "从-下单时间")
    @JsonProperty("start_place_order_time")
    private Date startPlaceOrderTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "到-下单时间")
    @JsonProperty("end_place_order_time")
    private Date endPlaceOrderTime;

    @ApiModelProperty(value = "订单code")
    @JsonProperty("order_code")
    private String orderCode;

    @ApiModelProperty(value = "订单状态")
    @JsonProperty("order_status_list")
    private List<Integer> orderStatusList;

    @ApiModelProperty(value = "下单人")
    @JsonProperty("place_order_by")
    private String placeOrderBy;


    @ApiModelProperty(value = "门店id")
    @JsonProperty("store_id")
    private String storeId;
}
