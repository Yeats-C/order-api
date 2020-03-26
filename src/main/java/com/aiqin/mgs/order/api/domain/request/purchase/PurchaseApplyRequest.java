package com.aiqin.mgs.order.api.domain.request.purchase;


import com.aiqin.mgs.order.api.base.PagesRequest;
import com.aiqin.mgs.order.api.domain.response.PurchaseGroupVo;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Data
public class PurchaseApplyRequest extends PagesRequest {

    @ApiModelProperty(value="采购组编码")
    @JsonProperty("purchase_group_code")
    private String purchaseGroupCode;

    @ApiModelProperty(value="供应商编码")
    @JsonProperty("supplier_code")
    private String supplierCode;

    @ApiModelProperty(value="仓库编码")
    @JsonProperty("transport_center_code")
    private String transportCenterCode;

    @ApiModelProperty(value="采购单号")
    @JsonProperty("purchase_order_code")
    private String purchaseOrderCode;

    @ApiModelProperty(value="库房编码")
    @JsonProperty("warehouse_code")
    private String warehouseCode;

    @ApiModelProperty(value="采购单状态 0.待确认1.完成 2.取消")
    @JsonProperty("purchase_order_status")
    private Integer purchaseOrderStatus;

    @ApiModelProperty(value="采购方式 0.直送 1.配送  2.铺采直送")
    @JsonProperty("purchase_mode")
    private Integer purchaseMode;

    @ApiModelProperty(value="创建开始时间")
    @JsonProperty("begin_time")
    private String beginTime;

    @ApiModelProperty(value="创建结束时间")
    @JsonProperty("finish_time")
    private String finishTime;

    @ApiModelProperty(value="修改开始时间")
    @JsonProperty("update_begin_time")
    private String updateBeginTime;

    @ApiModelProperty(value="修改结束时间")
    @JsonProperty("update_finish_time")
    private String updateFinishTime;

    @ApiModelProperty(value="采购单来源单号")
    @JsonProperty("source_code")
    private String sourceCode;

    public PurchaseApplyRequest() {
    }

    public PurchaseApplyRequest(String beginTime, String finishTime,
                                String purchaseOrderCode, String supplierCode, String transportCenterCode, String warehouseCode,
                                Integer purchaseOrderStatus, String sourceCode, Integer purchaseMode) {
        this.beginTime = beginTime;
        this.finishTime = finishTime;
        this.purchaseOrderCode = purchaseOrderCode;
        this.supplierCode = supplierCode;
        this.transportCenterCode = transportCenterCode;
        this.warehouseCode = warehouseCode;
        this.purchaseOrderStatus = purchaseOrderStatus;
        this.sourceCode = sourceCode;
        this.purchaseMode = purchaseMode;
    }
}
