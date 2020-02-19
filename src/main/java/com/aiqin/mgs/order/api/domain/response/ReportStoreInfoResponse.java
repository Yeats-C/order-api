package com.aiqin.mgs.order.api.domain.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * description: ReportStoreInfoResponse
 * date: 2020/2/17 20:00
 * author: hantao
 * version: 1.0
 */
@Data
@ApiModel("门店经营饼状图报表")
public class ReportStoreInfoResponse implements Serializable {

    @ApiModelProperty(value = "门店编码")
    @JsonProperty("store_code")
    private String storeCode;

    @ApiModelProperty(value = "数量")
    @JsonProperty("num")
    private Integer num;


}
