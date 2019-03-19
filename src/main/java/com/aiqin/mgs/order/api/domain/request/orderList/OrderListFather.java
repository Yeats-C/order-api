package com.aiqin.mgs.order.api.domain.request.orderList;

import com.aiqin.mgs.order.api.domain.OrderList;
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
 * @create 2019-01-24 17:34
 */
@ApiModel("父订单")
@Data
public class OrderListFather {

    @ApiModelProperty(value = "父订单编号")
    @JsonProperty("order_code_father")
    private String orderCodeFather;

    @ApiModelProperty(value = "应实付金额")
    @JsonProperty("actual_amount_paid_father")
    private Long actualAmountPaidFather;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "创建时间")
    @JsonProperty("create_time_father")
    private Date createTimeFather;

    @ApiModelProperty(value = "子订单")
    @JsonProperty("order_list")
    private List<OrderList> orderList;

    @ApiModelProperty(value = "是否能编辑")
    @JsonProperty("can_update")
    private Boolean canUpdate = false;
}
