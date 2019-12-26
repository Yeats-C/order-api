package com.aiqin.mgs.order.api.domain.request.order;

import com.aiqin.mgs.order.api.domain.po.order.ErpOrderInfo;
import com.aiqin.mgs.order.api.domain.po.order.ErpOrderLogistics;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 订单发货请求参数类
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/12/9 20:27
 */
@Data
public class ErpOrderDeliverRequest {

    @ApiModelProperty(value = "订单物流信息")
    @JsonProperty("order_logistics")
    private ErpOrderLogistics orderLogistics;

    @ApiModelProperty(value = "订单信息")
    @JsonProperty("order_list")
    private List<ErpOrderInfo> orderList;
}
