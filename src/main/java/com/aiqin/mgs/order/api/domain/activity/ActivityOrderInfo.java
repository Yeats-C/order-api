package com.aiqin.mgs.order.api.domain.activity;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

public class ActivityOrderInfo {

    @JsonProperty("order_code")
    @ApiModelProperty("订单编号")
    private String orderCode;

    @JsonProperty("sku_codes")
    @ApiModelProperty("sku编码集合")
    private List<String> skuCodes;

    @ApiModelProperty(value = "活动名称")
    @JsonProperty("activity_name")
    private String activityName;

    @ApiModelProperty(value = "活动编号，用来显示")
    @JsonProperty("activity_num")
    private String activityNum;

    @ApiModelProperty(value = "订单实付金额")
    @JsonProperty("sale_amount")
    private String saleAmount;

    @JsonProperty("member_name")
    @ApiModelProperty("会员名称（客户姓名）")
    private String memberName;

    @JsonProperty("member_phone")
    @ApiModelProperty("会员电话（客户电话）")
    private String memberPhone;

    @JsonProperty("create_time")
    @ApiModelProperty("下单时间")
    private String createTime;


}
