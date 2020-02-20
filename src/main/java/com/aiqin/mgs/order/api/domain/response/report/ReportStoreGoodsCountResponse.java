package com.aiqin.mgs.order.api.domain.response.report;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * description: ReportStoreGoodsCountResponse
 * date: 2020/2/19 19:45
 * author: hantao
 * version: 1.0
 */
@Data
@ApiModel("门店补货列表统计报表")
public class ReportStoreGoodsCountResponse implements Serializable {

    @ApiModelProperty(value = "数量")
    private Integer proCount;

    @ApiModelProperty(value = "品牌编码")
    private String productBrandCode;

    @ApiModelProperty(value = "品牌名称")
    private String productBrandName;

}
