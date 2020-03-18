package com.aiqin.mgs.order.api.domain.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * description: ReportAreaReturnSituationVo
 * date: 2020/3/2 19:10
 * author: hantao
 * version: 1.0
 */
@Data
@ApiModel("各地区退货情况统计表请求类")
public class ReportAreaReturnSituationVo implements Serializable {

    @ApiModelProperty("订单类型 1直送 2配送 3货架 ")
    @JsonProperty("type")
    private Integer type;

    @ApiModelProperty("退货理由")
    @JsonProperty("reason_code")
    private String reasonCode;

    @ApiModelProperty("门店编码集合（后端填充）")
    @JsonProperty("store_codes")
    private List<String> storeCodes;

    @ApiModelProperty("品类编码")
    @JsonProperty("category_code")
    private String categoryCode;


}
