package com.aiqin.mgs.order.api.domain.request.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author: zhao shuai
 * @create: 2019-12-23
 **/
@Data
public class StockLockDetailRequest {

    @ApiModelProperty("省编码")
    @JsonProperty("province_code")
    private String provinceCode;

    @ApiModelProperty("市编码")
    @JsonProperty("city_code")
    private String cityCode;

    @ApiModelProperty("sku编码")
    @JsonProperty("sku_code")
    private String skuCode;

    @ApiModelProperty("锁库数量")
    @JsonProperty("change_count")
    private Integer changeCount;

    @ApiModelProperty("批次信息")
    @JsonProperty(value = "batch_list")
    private List<StockBatchInfoRequest> batchList;
}
