package com.aiqin.mgs.order.api.domain.response;

import com.aiqin.mgs.order.api.domain.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
@ApiModel
@Data
public class RejectVoResponse {

    @ApiModelProperty(value = "退供单详情list（商品列表）")
    @JsonProperty("details")
    private List<RejectRecordDetail> details;

    @ApiModelProperty(value = "退供单信息")
    @JsonProperty("rejectRecord")
    private RejectRecordVo rejectRecordVo;

    @ApiModelProperty(value = "商品批次列表")
    @JsonProperty("rejectRecordDetailBatches")
    private List<RejectRecordDetailBatch> rejectRecordDetailBatches;
}
