package com.aiqin.mgs.order.api.domain.response;

import com.aiqin.mgs.order.api.domain.ReturnOrderDetail;
import com.aiqin.mgs.order.api.domain.ReturnOrderDetailBatch;
import com.aiqin.mgs.order.api.domain.ReturnOrderInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
@ApiModel
@Data
public class ReturnOrderResponse {
    @ApiModelProperty(value = "退货单详情list（商品列表）")
    @JsonProperty("details")
    private List<ReturnOrderDetail> details;

    @ApiModelProperty(value = "退货单信息")
    @JsonProperty("returnOrderInfo")
    private ReturnOrderInfo returnOrderInfo;

    @ApiModelProperty(value = "商品批次列表")
    @JsonProperty("returnOrderDetailBatches")
    private List<ReturnOrderDetailBatch> returnOrderDetailBatches;
}
