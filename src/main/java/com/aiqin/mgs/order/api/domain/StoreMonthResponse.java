package com.aiqin.mgs.order.api.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@JsonInclude(JsonInclude.Include.ALWAYS)
@ApiModel("门店销量信息")
public class StoreMonthResponse {

    @ApiModelProperty(value="门店编码")
    @JsonProperty("store_code")
    private String storeCode;

    @ApiModelProperty(value="门店名称")
    @JsonProperty("store_name")
    private String storeName;

    @ApiModelProperty(value="实付金额(上月销量)")
    @JsonProperty("pay_money")
    private BigDecimal payMoney;
}
