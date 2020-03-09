package com.aiqin.mgs.order.api.domain.request.purchase;

import com.aiqin.mgs.order.api.base.PagesRequest;
import com.aiqin.mgs.order.api.domain.response.PurchaseGroupVo;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("退供单查询类")
public class RejectQueryRequest extends PagesRequest {
    @ApiModelProperty(value="开始时间")
    @JsonProperty("begin_time")
    private String beginTime;

    @ApiModelProperty(value="结束时间")
    @JsonProperty("finish_time")
    private String finishTime;

    @ApiModelProperty(value="退供单号")
    @JsonProperty("reject_record_code")
    private String rejectRecordCode;

    @ApiModelProperty(value="采购组 code")
    @JsonProperty("purchase_group_code")
    private String purchaseGroupCode;

    @ApiModelProperty(value="供应商编码")
    @JsonProperty("supplier_code")
    private String supplierCode;

    @ApiModelProperty(value="仓库编码")
    @JsonProperty("transport_center_code")
    private String transportCenterCode;

    @ApiModelProperty(value = "库房编码")
    @JsonProperty("warehouse_code")
    private String warehouseCode;

    @ApiModelProperty(value = "退供单状态: 0 待审核 1 审核中  2 待供应商确认 3 待出库  4 出库开始 5 已出库 6 已发运 7 完成 8 取消 9 审核不通过")
    @JsonProperty("reject_status")
    private Integer rejectStatus;

    @ApiModelProperty()
    private List<PurchaseGroupVo> groupList;

    public RejectQueryRequest() {
    }

    public RejectQueryRequest(String beginTime, String finishTime, String rejectRecordCode, String purchaseGroupCode, String supplierCode, String transportCenterCode, String warehouseCode, Integer rejectStatus) {
        this.beginTime = beginTime;
        this.finishTime = finishTime;
        this.rejectRecordCode = rejectRecordCode;
        this.purchaseGroupCode = purchaseGroupCode;
        this.supplierCode = supplierCode;
        this.transportCenterCode = transportCenterCode;
        this.warehouseCode = warehouseCode;
        this.rejectStatus = rejectStatus;
    }
}
