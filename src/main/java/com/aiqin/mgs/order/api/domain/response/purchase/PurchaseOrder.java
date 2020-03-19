package com.aiqin.mgs.order.api.domain.response.purchase;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@ApiModel
@Data
public class PurchaseOrder {
    @ApiModelProperty(value="")
    @JsonProperty("id")
    private Long id;

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

    @ApiModelProperty(value="最小单位数量")
    @JsonProperty("total_count")
    private Integer totalCount;

    @ApiModelProperty(value="商品含税金额")
    @JsonProperty("total_tax_amount")
    private BigDecimal totalTaxAmount;

    @ApiModelProperty(value="采购方式 0 配送  1.铺采直送")
    @JsonProperty("purchase_mode")
    private Integer purchaseMode;

    @ApiModelProperty(value="结算方式编码")
    @JsonProperty("settlement_method_code")
    private String settlementMethodCode;

    @ApiModelProperty(value="结算方式名称")
    @JsonProperty("settlement_method_name")
    private String settlementMethodName;

    @ApiModelProperty(value="取消原因")
    @JsonProperty("cancel_reason")
    private String cancelReason;

    @ApiModelProperty(value="取消备注")
    @JsonProperty("cancel_remark")
    private String cancelRemark;

    @ApiModelProperty(value="采购单的类型（手动，自动）")
    @JsonProperty("apply_type_form")
    private String applyTypeForm;

    @ApiModelProperty(value="关联审批单")
    @JsonProperty("approval_code")
    private String approvalCode;

    @ApiModelProperty(value="公司编码")
    @JsonProperty("company_code")
    private String companyCode;

    @ApiModelProperty(value="公司名称")
    @JsonProperty("company_name")
    private String companyName;

    @ApiModelProperty(value="来源类型 0.采购申请 1.订单")
    @JsonProperty("purchase_source")
    private Integer purchaseSource;

    @ApiModelProperty(value="预计到货时间")
    @JsonProperty("pre_arrival_time")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date preArrivalTime;

    @ApiModelProperty(value="有效期")
    @JsonProperty("valid_time")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date validTime;

    @ApiModelProperty(value="实际最小单位数量")
    @JsonProperty("actual_total_count")
    private Long actualTotalCount;

    @ApiModelProperty(value="实际商品数量")
    @JsonProperty("actual_total_tax_amount")
    private BigDecimal actualTotalTaxAmount;

    @ApiModelProperty(value="负责人")
    @JsonProperty("charge_person")
    private String charge_person;

    @ApiModelProperty(value="联系人（供应商的联系人）")
    @JsonProperty("contact_person")
    private String contactPerson;

    @ApiModelProperty(value="供应商电话")
    @JsonProperty("contact_mobile")
    private String contactMobile;

    @ApiModelProperty(value="发货时间")
    @JsonProperty("delivery_time")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date deliveryTime;

    @ApiModelProperty(value="入库时间")
    @JsonProperty("in_stock_time")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date inStockTime;

    @ApiModelProperty(value="付款方式 0.预付款 1.货到付款 2.月结 3.实销实结")
    @JsonProperty("payment_code")
    private Integer paymentCode;

    @ApiModelProperty(value="付款方式 0.预付款 1.货到付款 2.月结 3.实销实结")
    @JsonProperty("payment_name")
    private String paymentName;

    @ApiModelProperty(value="付款期")
    @JsonProperty("payment_time")
    private Integer paymentTime;

    @ApiModelProperty(value="备注")
    @JsonProperty("remark")
    private String remark;

    @ApiModelProperty(value="最多入库次数")
    @JsonProperty("inbound_line")
    private Integer inboundLine;

    @ApiModelProperty(value="预付款金额")
    @JsonProperty("pre_amount")
    private BigDecimal preAmount;

    @ApiModelProperty(value="账户编码")
    @JsonProperty("account_code")
    private String accountCode;

    @ApiModelProperty(value="账户名称")
    @JsonProperty("account_name")
    private String accountName;

    @ApiModelProperty(value="合同编码")
    @JsonProperty("contract_code")
    private String contractCode;

    @ApiModelProperty(value="合同名称")
    @JsonProperty("contract_name")
    private String contractName;

    @ApiModelProperty(value="创建时间")
    @JsonProperty("create_time")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @ApiModelProperty(value="修改时间")
    @JsonProperty("update_time")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    @ApiModelProperty(value="创建人id")
    @JsonProperty("create_by_id")
    private String createById;

    @ApiModelProperty(value="修改人id")
    @JsonProperty("update_by_id")
    private String updateById;

    @ApiModelProperty(value="创建人名称")
    @JsonProperty("create_by_name")
    private String createByName;

    @ApiModelProperty(value="修改人名称")
    @JsonProperty("update_by_name")
    private String updateByName;

    @ApiModelProperty(value="0:未同步,1已同步")
    @JsonProperty("synchr_status")
    private Integer synchrStatus;

    @ApiModelProperty(value="来源单号")
    @JsonProperty("source_code")
    private String sourceCode;

}
