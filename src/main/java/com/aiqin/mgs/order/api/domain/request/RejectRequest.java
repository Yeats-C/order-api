package com.aiqin.mgs.order.api.domain.request;

import com.aiqin.mgs.order.api.base.PagesRequest;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

@Data
@ApiModel("退供条件")
@ToString
public class RejectRequest extends PagesRequest {
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private String beginTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private String finishTime;
    @ApiModelProperty(value = "退供单号")
    @JsonProperty("reject_record_code")
    private String rejectRecordCode;

    @ApiModelProperty(value = "采购组名称")
    @JsonProperty("purchase_group_name")
    private String purchaseGroupName;

    @ApiModelProperty(value = "供应商名称")
    @JsonProperty("supplier_name")
    private String supplierName;

    @ApiModelProperty(value = "仓库名称")
    @JsonProperty("transport_center_name")
    private String transportCenterName;
    @ApiModelProperty(value = "库房名称")
    @JsonProperty("warehouse_name")
    private String warehouseName;
    @ApiModelProperty(value = "退供单状态")
    @JsonProperty("reject_record_status")
    private Integer rejectRecordStatus;
    @ApiModelProperty(value = "公司编码")
    @JsonProperty("company_code")
    private String companyCode;

    public RejectRequest(String beginTime, String finishTime, String rejectRecordCode, String purchaseGroupName, String supplierName, String transportCenterName, String warehouseName, Integer rejectRecordStatus,String companyCode ) {
        this.beginTime = beginTime;
        this.finishTime = finishTime;
        this.rejectRecordCode = rejectRecordCode;
        this.purchaseGroupName = purchaseGroupName;
        this.supplierName = supplierName;
        this.transportCenterName = transportCenterName;
        this.warehouseName = warehouseName;
        this.rejectRecordStatus = rejectRecordStatus;
        this.companyCode = companyCode;
    }
}
