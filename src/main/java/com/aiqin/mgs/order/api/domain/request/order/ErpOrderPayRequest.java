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

    /***订单编号*/
    @ApiModelProperty(value = "订单编号")
    private String orderCode;

    /***支付方式*/
    @ApiModelProperty(value = "支付方式")
    private Integer payWay;

    /***物流券id，多个用逗号间隔*/
    @ApiModelProperty(value = "物流券id")
    private String couponIds;
}
