package com.aiqin.mgs.order.api.domain.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author : ZhangJunJie
 * @Data : 2020-04-30
 * @Description :
 */
@Data
@ApiModel("修改门店可用额度信息")
public class StoreQuotaRequest {

    @ApiModelProperty(value = "门店编码")
    @JsonProperty("store_code")
    private String storeCode;

    @ApiModelProperty(value = "赠品额度积分")
    @JsonProperty("complimentary_amount")
    private BigDecimal complimentaryAmount;



}
