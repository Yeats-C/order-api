package com.aiqin.mgs.order.api.domain.response.report;

import com.aiqin.mgs.order.api.base.PageResData;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * description: ReportOrderAndStoreResponse
 * date: 2020/2/20 19:44
 * author: hantao
 * version: 1.0
 */
@Data
@ApiModel("订单门店报表列表返回实体类")
public class ReportOrderAndStoreListResponse implements Serializable {

    @ApiModelProperty(value = "总销量")
    @JsonProperty("total_num")
    private Long totalNum;

    @ApiModelProperty(value = "总额")
    @JsonProperty("total_amount")
    private BigDecimal totalAmount;

    @ApiModelProperty(value = "总的同比")
    @JsonProperty("total_tong_ratio")
    private BigDecimal totalTongRatio;

    @ApiModelProperty(value = "总的环比")
    @JsonProperty("total_chain_ratio")
    private BigDecimal totalChainRatio;

    @ApiModelProperty(value = "列表数据")
    @JsonProperty("page_res_data")
    private PageResData pageResData;

}
