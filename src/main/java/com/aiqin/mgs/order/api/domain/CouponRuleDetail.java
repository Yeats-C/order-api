package com.aiqin.mgs.order.api.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel("优惠券优惠规则--商品属性关系表")
public class CouponRuleDetail {

    @ApiModelProperty(value = "编码")
    @JsonProperty("id")
    private Long id;

    @ApiModelProperty(value = "优惠券类型 0-物流券 1-服纺券 2-A品券")
    @JsonProperty("coupon_type")
    private Byte couponType;

    @ApiModelProperty(value = "商品属性编码")
    @JsonProperty("product_property_code")
    private String productPropertyCode;

    @ApiModelProperty(value = "商品属性名称")
    @JsonProperty("product_property_name")
    private String productPropertyName;

    @ApiModelProperty(value = "创建时间")
    @JsonProperty("create_time")
    private Date createTime;

}