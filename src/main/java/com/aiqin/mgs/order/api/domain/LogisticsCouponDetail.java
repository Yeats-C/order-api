package com.aiqin.mgs.order.api.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 物流券详情
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2020/1/7 16:32
 */
@Data
public class LogisticsCouponDetail {

    @ApiModelProperty(value = "优惠券编码")
    @JsonProperty("coupon_code")
    private String couponCode;

    @ApiModelProperty(value = "面值")
    @JsonProperty("nominal_Value")
    private BigDecimal nominalValue;

    @ApiModelProperty(value = "使用状态 0未使用 1已使用 2过期")
    @JsonProperty("active_condition")
    private Integer activeCondition;

}
