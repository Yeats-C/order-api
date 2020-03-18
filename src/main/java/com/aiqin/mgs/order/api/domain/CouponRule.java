package com.aiqin.mgs.order.api.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@ApiModel("优惠券优惠规则")
public class CouponRule {

    @ApiModelProperty(value = "id")
    @JsonProperty("id")
    private Long id;

    @ApiModelProperty(value = "优惠券类型 0-物流券 1-服纺券 2-A品券")
    @JsonProperty("coupon_type")
    private Integer couponType;

    @ApiModelProperty(value = "优惠券名称")
    @JsonProperty("coupon_name")
    private String couponName;

    @ApiModelProperty(value = "优惠比例")
    @JsonProperty("proportion")
    private BigDecimal proportion;

    @ApiModelProperty(value = "创建时间")
    @JsonProperty("create_time")
    private Date createTime;

    @ApiModelProperty(value = "修改时间")
    @JsonProperty("update_time")
    private Date updateTime;

    @ApiModelProperty(value = "封装商品属性集合")
    @JsonProperty("coupon_rule_detail_list")
    private List<CouponRuleDetail> couponRuleDetailList;

}