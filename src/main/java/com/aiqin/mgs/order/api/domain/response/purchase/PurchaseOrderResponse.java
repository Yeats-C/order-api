package com.aiqin.mgs.order.api.domain.response.purchase;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;


/**
 * @Author xieq
 * @Date 2020/2/27
 */
@Data
public class PurchaseOrderResponse {

    @ApiModelProperty(value="采购单id")
    @JsonProperty("purchase_order_id")
    private String purchaseOrderId;

    @ApiModelProperty(value="采购单号")
    @JsonProperty("purchase_order_code")
    private String purchaseOrderCode;

    @ApiModelProperty(value="仓库编码")
    @JsonProperty("transport_center_code")
    private String transportCenterCode;

    @ApiModelProperty(value="仓库名称")
    @JsonProperty("transport_center_name")
    private String transportCenterName;

    @ApiModelProperty(value="采购组编码")
    @JsonProperty("purchase_group_code")
    private String purchaseGroupCode;

    @ApiModelProperty(value="采购组名称")
    @JsonProperty("purchase_group_name")
    private String purchaseGroupName;

    @ApiModelProperty(value="库房编码")
    @JsonProperty("warehouse_code")
    private String warehouseCode;

    @ApiModelProperty(value="库房名称")
    @JsonProperty("warehouse_name")
    private String warehouseName;

    @ApiModelProperty(value="供应商编码")
    @JsonProperty("supplier_code")
    private String supplierCode;

    @ApiModelProperty(value="供应商名称")
    @JsonProperty("supplier_name")
    private String supplierName;

    @ApiModelProperty(value="采购信息完善状态 0.未提交  1.已提交")
    @JsonProperty("info_status")
    private Integer infoStatus;

    @ApiModelProperty(value="采购单状态 0.待审核 1.审核中 2.审核通过  3.备货确认 4.发货确认  5.入库开始 6.入库中 7.已入库  8.完成 9.取消 10.审核不通过")
    @JsonProperty("purchase_order_status")
    private Integer purchaseOrderStatus;

    @ApiModelProperty(value="仓储状态 0.未开始  1.确认中 2.完成")
    @JsonProperty("storage_status")
    private Integer storageStatus;

    @ApiModelProperty(value="单品数量")
    @JsonProperty("single_count")
    private Integer singleCount;

    @ApiModelProperty(value="含税采购金额")
    @JsonProperty("product_total_amount")
    private BigDecimal productTotalAmount;

    @ApiModelProperty(value="实物返金额")
    @JsonProperty("return_amount")
    private BigDecimal returnAmount;

    @ApiModelProperty(value="采购方式 0 配送  1.铺采直送")
    @JsonProperty("purchase_mode")
    private Integer purchaseMode;

    @ApiModelProperty(value="结算方式编码")
    @JsonProperty("settlement_method_code")
    private String settlementMethodCode;

    @ApiModelProperty(value="结算方式名称")
    @JsonProperty("settlement_method_name")
    private String settlementMethodName;

    @ApiModelProperty(value="创建时间")
    @JsonProperty("create_time")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @ApiModelProperty(value="修改时间")
    @JsonProperty("update_time")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    @ApiModelProperty(value="创建者")
    @JsonProperty("create_by_name")
    private String createByName;

    @ApiModelProperty(value="实际单品数量")
    @JsonProperty("actual_single_count")
    private Integer actualSingleCount;

    @ApiModelProperty(value="实际含税采购金额")
    @JsonProperty("actual_total_amount")
    private BigDecimal actualTotalAmount;

    @ApiModelProperty(value="实际实物返金额")
    @JsonProperty("actual_return_amount")
    private BigDecimal actualReturnAmount;

    @ApiModelProperty(value="预计到货时间")
    @JsonProperty("expect_arrival_time")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date expectArrivalTime;

    @ApiModelProperty(value="有效期")
    @JsonProperty("valid_time")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date validTime;

    @ApiModelProperty(value="创建者id")
    @JsonProperty("create_by_id")
    private String createById;

    @ApiModelProperty(value="修改者id")
    @JsonProperty("update_by_id")
    private String updateById;

    @ApiModelProperty(value="修改者")
    @JsonProperty("update_by_name")
    private String updateByName;

    @ApiModelProperty(value="赠品含税金额")
    @JsonProperty("gift_tax_sum")
    private BigDecimal giftTaxSum;

    @ApiModelProperty(value="实际赠品含税金额")
    @JsonProperty("actual_gift_tax_sum")
    private BigDecimal actualGiftTaxSum;

    @ApiModelProperty(value="关联审批单")
    @JsonProperty("approval_code")
    private String approvalCode;

    @ApiModelProperty(value="采购单类型编码 1 普通采购 2 预采购")
    @JsonProperty("purchase_order_type_code")
    private Integer purchaseOrderTypeCode;

    @ApiModelProperty(value="采购单类型名称")
    @JsonProperty("purchase_order_type_name")
    private String purchaseOrderTypeName;

    @ApiModelProperty(value="预采购单号")
    @JsonProperty("purchase_order_pre")
    private String purchaseOrderPre;
}
