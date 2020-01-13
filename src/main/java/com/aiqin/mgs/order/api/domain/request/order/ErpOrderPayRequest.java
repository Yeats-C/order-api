package com.aiqin.mgs.order.api.domain.request.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

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
    @JsonProperty("order_code")
    private String orderCode;

    @ApiModelProperty(value = "支付方式 支付订单费用时使用")
    @JsonProperty("pay_way")
    private Integer payWay;

    @ApiModelProperty(value = "物流券唯一标识，可能是物流券编码（多个） 支付物流费用时使用")
    @JsonProperty("coupon_ids")
    private List<String> couponIds;

    @ApiModelProperty(value = "用户id")
    @JsonProperty("person_id")
    private String personId;

    @ApiModelProperty(value = "用户名称")
    @JsonProperty("person_name")
    private String personName;
}
