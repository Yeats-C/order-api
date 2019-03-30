package com.aiqin.mgs.order.api.domain.request.orderList;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 描述:
 *
 * @author zhujunchao
 * @create 2019-03-07 17:50
 */
@ApiModel("发货订单变更vo")
@Data
public class DeliverVo {
    @ApiModelProperty(value = "订单编号")
    @JsonProperty("order_code")
    private String orderCode;

    @ApiModelProperty(value = "发货单号")
    @JsonProperty("invoice_code")
    private String invoiceCode;

    @ApiModelProperty(value = "执行人")
    @JsonProperty("implement_by")
    private String implementBy;

//    @ApiModelProperty(value = "订单商品ID")
//    @JsonProperty("order_product_id")
//    private String orderProductId;

    @ApiModelProperty(value = "添加商品实发数量list")
    @JsonProperty("actual_deliver_list")
    private List<ActualDeliverVo> actualDeliverVos;

}
