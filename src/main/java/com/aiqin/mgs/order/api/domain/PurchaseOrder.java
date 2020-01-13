package com.aiqin.mgs.order.api.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 采购单
 * TableName: purchase_order
 */
@Data
@ToString(callSuper = true)
@ApiModel("采购单")
public class PurchaseOrder implements Serializable {
    private long id;
    @ApiModelProperty(value = "业务id")
    private String purchaseOrderId;
    @ApiModelProperty(value = "采购单号")
    private String purchaseOrderCode;
    @ApiModelProperty(value = "公司编码")
    private String companyCode;
    @ApiModelProperty(value = "公司名称")
    private String companyName;
    @ApiModelProperty(value = "供应商编码")
    private String supplierCode;
    @ApiModelProperty(value = "供应商名称")
    private String supplierName;
    @ApiModelProperty(value = "仓库编码")
    private String transportCenterCode;
    @ApiModelProperty(value = "仓库名称")
    private String transportCenterName;
    @ApiModelProperty(value = "库房编码")
    private String warehouseCode;
    @ApiModelProperty(value = "库房名称")
    private String warehouseName;
    @ApiModelProperty(value = "采购组编码")
    private String purchaseGroupCode;
    @ApiModelProperty(value = "采购组名称")
    private String purchaseGroupName;
    @ApiModelProperty(value = "结算方式编码")
    private String settlementMethodCode;
    @ApiModelProperty(value = "结算方式名称")
    private String settlementMethodName;
    @ApiModelProperty(value = "采购单状态  0.待审核 1.审核中 2.审核通过  3.备货确认 4.发货确认  5.入库开始 6.入库中 7.已入库  8.完成 9.取消 10.审核不通过")
    private Integer purchaseOrderStatus;
    @ApiModelProperty(value = "采购方式 0. 铺采直送  1.配送")
    private Integer purchaseMode;
    @ApiModelProperty(value = "采购单类型   0手动，1.自动")
    private Integer purchaseType;
    @ApiModelProperty(value = "最小单位数量")
    private Long totalCount;
    @ApiModelProperty(value = "商品含税总金额")
    private BigDecimal totalTaxAmount;
    @ApiModelProperty(value = "实际最小单数数量")
    private Long actualTotalCount;
    @ApiModelProperty(value = "实际商品含税总金额")
    private BigDecimal actualTotalTaxAmount;
    @ApiModelProperty(value = "取消原因")
    private String cancelReason;
    @ApiModelProperty(value = "取消备注")
    private String cancelRemark;
    @ApiModelProperty(value = "0. 启用   1.禁用")
    private Integer useStatus;
    @ApiModelProperty(value = "负责人")
    private String chargePerson;
    @ApiModelProperty(value = "账户编码")
    private String accountCode;
    @ApiModelProperty(value = "账户名称")
    private String accountName;
    @ApiModelProperty(value = "合同编码")
    private String contractCode;
    @ApiModelProperty(value = "合同名称")
    private String contractName;
    @ApiModelProperty(value = "联系人")
    private String contactPerson;
    @ApiModelProperty(value = "联系人电话")
    private String contactMobile;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date preArrivalTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date validTime;
    @ApiModelProperty(value = "发货地址")
    private String deliveryAddress;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date deliveryTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date inStockTime;
    @ApiModelProperty(value = "入库地址")
    private String inStockAddress;
    @ApiModelProperty(value = "预采购单号")
    private String prePurchaseOrder;
    @ApiModelProperty(value = "付款方式编码")
    private String paymentCode;
    @ApiModelProperty(value = "付款方式名称")
    private String paymentName;
    @ApiModelProperty(value = "付款期")
    private Date paymentTime;
    @ApiModelProperty(value = "预付付款金额")
    private BigDecimal preAmount;
    @ApiModelProperty(value = "备注")
    private String remark;
    @ApiModelProperty(value = "来源单号")
    private String sourceCode;
    @ApiModelProperty(value = "来源类型")
    private Integer sourceType;
    @ApiModelProperty(value = "创建人编码")
    private String createById;
    @ApiModelProperty(value = "创建人名称")
    private String createByName;
    @ApiModelProperty(value = "修改人编码")
    private String updateById;
    @ApiModelProperty(value = "修改人名称")
    private String updateByName;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;
}