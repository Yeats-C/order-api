package com.aiqin.mgs.order.api.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@ApiModel("首单赠送--赠送市值核对表")
public class OrderGiveFee {

    @ApiModelProperty(value="id")
    @JsonProperty("id")
    private Long id;

    @ApiModelProperty(value="订单编码")
    @JsonProperty("order_code")
    private String orderCode;

    @ApiModelProperty(value="门店id")
    @JsonProperty("store_id")
    private String storeId;

    @ApiModelProperty(value="扣减金额")
    @JsonProperty("amount")
    private BigDecimal amount;

    @ApiModelProperty(value="金额差值")
    @JsonProperty("sub_amount")
    private BigDecimal subAmount;

    @ApiModelProperty(value="核对状态 0:未核对 1:已核对")
    @JsonProperty("status")
    private Integer status;

    @ApiModelProperty(value="创建时间")
    @JsonProperty("create_time")
    private Date createTime;

    @ApiModelProperty(value="修改时间")
    @JsonProperty("update_time")
    private Date updateTime;

}