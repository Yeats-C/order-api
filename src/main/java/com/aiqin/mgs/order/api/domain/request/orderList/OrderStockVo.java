package com.aiqin.mgs.order.api.domain.request.orderList;

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
 * @create 2019-01-11 11:12
 */
@ApiModel("订单查询一段时间进货量vo")
@Data
public class OrderStockVo {
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "从-下单时间")
    @JsonProperty("start_place_order_time")
    private Date startPlaceOrderTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "到-下单时间")
    @JsonProperty("end_place_order_time")
    private Date endPlaceOrderTime;

    @ApiModelProperty(value = "门店code")
    @JsonProperty("store_code")
    private List<String> storeCode;


}
