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
 * @create 2019-01-04 17:23
 */
@ApiModel("订单后台查询vo")
@Data
public class OrderListVo extends PagesRequest {
    @ApiModelProperty(value = "门店code/名称")
    @JsonProperty("store_code")
    private String storeCode;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "从-下单时间")
    @JsonProperty("start_place_order_time")
    private Date startPlaceOrderTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "到-下单时间")
    @JsonProperty("end_place_order_time")
    private Date endPlaceOrderTime;

    @ApiModelProperty(value = "支付状态")
    @JsonProperty("payment_status")
    private Integer paymentStatus;

    @ApiModelProperty(value = "订单状态")
    @JsonProperty("order_status_list")
    private List<Integer> orderStatusList;

    @ApiModelProperty(value = "门店类型")
    @JsonProperty("store_type")
    private String storeType;
}
