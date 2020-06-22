package com.aiqin.mgs.order.api.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value = "物流减免列表条件信息")
public class LogisticsRuleRequest {

    @ApiModelProperty(value = "商品编码")
    @JsonProperty("product_code")
    private String productCode;

    @ApiModelProperty(value = "商品名称")
    @JsonProperty("product_name")
    private String productName;

    @ApiModelProperty(value = "生效状态 0未开 1开启")
    @JsonProperty("effective_status")
    private Integer effectiveStatus;

    @ApiModelProperty(value = "每页条数")
    @JsonProperty("page_size")
    private Integer pageSize;

    @ApiModelProperty(value = "当前页")
    @JsonProperty("page_no")
    private Integer pageNo;

    @ApiModelProperty(value = "spu集合")
    @JsonProperty("spu_codes")
    private List<String> spuCodes;

}
