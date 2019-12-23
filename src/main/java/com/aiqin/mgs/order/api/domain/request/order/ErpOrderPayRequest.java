package com.aiqin.mgs.order.api.domain.request.order;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 订单支付请求参数
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/12/9 16:42
 */
@Data
public class ErpOrderPayRequest {

    @ApiModelProperty(value = "订单编号")
    private String orderCode;

    @ApiModelProperty(value = "支付方式")
    private Integer payWay;

    @ApiModelProperty(value = "物流券唯一标识，多个用逗号间隔")
    private String couponIds;
}
