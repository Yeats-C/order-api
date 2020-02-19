package com.aiqin.mgs.order.api.domain.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * description: ReportStoreGoodsVo
 * date: 2020/2/19 17:05
 * author: hantao
 * version: 1.0
 */
@Data
@ApiModel("门店补货报表查询")
public class ReportStoreGoodsVo implements Serializable {

    @ApiModelProperty(value = "查询时间")
    @JsonProperty("count_time")
    private String countTime;

}
