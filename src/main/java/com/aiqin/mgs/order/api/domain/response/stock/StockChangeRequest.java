package com.aiqin.mgs.order.api.domain.response.stock;

import com.aiqin.mgs.order.api.domain.request.product.StockBatchInfoRequest;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel("库存修改")
@Data
public class StockChangeRequest {

    @ApiModelProperty("操作类型 1.锁定库存 2.减库存并解锁 3.解锁库存. 4.减库存 5.加并锁定库存 6.加库存 " +
            "7.加在途 8.减在途 9.锁转移(锁定库存移入/移出)'")
    @JsonProperty(value = "operation_type")
    private Integer operationType;

    @ApiModelProperty("库存集合")
    @JsonProperty(value = "stock_list")
    private List<StockInfoRequest> stockList;

    @ApiModelProperty("批次库存集合")
    @JsonProperty(value = "stock_batch_list")
    private List<StockBatchInfoRequest> stockBatchList;
}
