package com.aiqin.mgs.order.api.domain.request.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 商品出货请求参数
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2020/1/1 15:45
 */
@Data
public class ErpOrderDeliverRequest {

    @ApiModelProperty(value = "订单号")
    @JsonProperty("order_code")
    private String orderCode;

    @ApiModelProperty(value = "发货日期")
    @JsonProperty("delivery_time")
    private Date deliveryTime;

    @ApiModelProperty(value = "出货明细数量")
    @JsonProperty("item_list")
    private List<ErpOrderDeliverItemRequest> itemList;

    @ApiModelProperty(value = "操作人id")
    @JsonProperty("person_id")
    private String personId;

    @ApiModelProperty(value = "操作人姓名")
    @JsonProperty("person_name")
    private String personName;

}
