package com.aiqin.mgs.order.api.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class RejectRecordInfo implements Serializable {
    //退单主表
    @ApiModelProperty(value = "退货单单号")
    private String returnOrderCode;
    @ApiModelProperty(value = "实际退货数量")
    private Long actualTotalCount;
    @ApiModelProperty(value = "退货时间")
    private Data updateTime;
    @ApiModelProperty(value = "退货人id")
    private String ypdateById;
    @ApiModelProperty(value = "退单明细")
    private List<RejectRecordDetailInfo> rejectRecordDetailInfo;
    @ApiModelProperty(value = "推供批次明细（仓卡）")
    private List<RejectRecordBatchDetailInfo> rejectRecordBatchDetailInfo;
}
