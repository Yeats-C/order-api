package com.aiqin.mgs.order.api.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;
/**
 * 退供单
 * TableName: reject_record
 */
@Data
@ToString(callSuper = true)
@ApiModel("退供单")
public class RejectRecord {
    @ApiModelProperty(value = "id")
    //@JsonProperty("id")
    private String id;
    @ApiModelProperty(value = "业务id")
    //@JsonProperty("reject_record_id")
    private String rejectRecordId;
    @ApiModelProperty(value = "退供单号")
    //@JsonProperty("reject_record_code")
    private String rejectRecordCode;
    @ApiModelProperty(value = "公司编码")
    //@JsonProperty("company_code")
    private String companyCode;
    @ApiModelProperty(value = "公司名称")
    //@JsonProperty("company_name")
    private String companyName;
    @ApiModelProperty(value = "供应商编码")
    //@JsonProperty("supplier_code")
    private String supplierCode;
    @ApiModelProperty(value = "供应商名称")
    //@JsonProperty("supplier_name")
    private String supplierName;
    @ApiModelProperty(value = "仓库编码")
    //@JsonProperty("transport_center_code")
    private String transportCenterCode;
    @ApiModelProperty(value = "仓库名称")
    //@JsonProperty("transport_center_name")
    private String transportCenterName;
    @ApiModelProperty(value = "库房编码")
    //@JsonProperty("warehouse_code")
    private String warehouseCode;
    @ApiModelProperty(value = "库房名称")
    //@JsonProperty("warehouse_name")
    private String warehouseName;
    @ApiModelProperty(value = "采购组编码")
    //@JsonProperty("purchase_group_code")
    private String purchaseGroupCode;
    @ApiModelProperty(value = "采购组名称")
    //@JsonProperty("purchase_group_name")
    private String purchaseGroupName;
    @ApiModelProperty(value = "结算方式编码")
    //@JsonProperty("settlement_method_code")
    private String settlementMethodCode;
    @ApiModelProperty(value = "结算方式名称")
    //@JsonProperty("settlement_method_name")
    private String settlementMethodName;
    @ApiModelProperty(value = "退供单状态")
    //@JsonProperty("reject_record_status")
    private Integer rejectRecordStatus;
    @ApiModelProperty(value = "最小单位数量")
    //@JsonProperty("total_count")
    private Long totalCount;
    @ApiModelProperty(value = "商品含税金额")
    //@JsonProperty("total_tax_amount")
    private BigDecimal totalTaxAmount;
    @ApiModelProperty(value = "实际最小单数数量")
    //@JsonProperty("actual_total_count")
    private Long actualTotalCount;
    @ApiModelProperty(value = "实际商品含税金额")
    //@JsonProperty("actual_total_tax_amount")
    private BigDecimal actualTotalTaxAmount;
    @ApiModelProperty(value = "负责人")
    //@JsonProperty("charge_person")
    private String chargePerson;
    @ApiModelProperty(value = "联系人")
    //@JsonProperty("contact_person")
    private String contactPerson;
    @ApiModelProperty(value = "联系人电话")
    //@JsonProperty("contact_mobile")
    private String contactMobile;
    @ApiModelProperty(value = "收货区域 :省编码")
    //@JsonProperty("province_id")
    private String provinceId;
    @ApiModelProperty(value = "收货区域 :省")
    //@JsonProperty("province_name")
    private String provinceName;
    @ApiModelProperty(value = "收货区域 :市编码")
    //@JsonProperty("city_id")
    private String cityId;
    @ApiModelProperty(value = "收货区域 :市")
    //@JsonProperty("city_name")
    private String cityName;
    @ApiModelProperty(value = "收货区域 :区/县")
    //@JsonProperty("district_id")
    private String districtId;
    @ApiModelProperty(value = "收货区域 :区/县")
    //@JsonProperty("district_name")
    private String districtName;
    @ApiModelProperty(value = "收货地址")
    //@JsonProperty("receive_address")
    private String receiveAddress;
    @ApiModelProperty(value = "预计发货时间",example = "2001-01-01 01:01:01")
    //@JsonProperty("pre_expect_time")
    private Date preExpectTime;
    @ApiModelProperty(value = "有效期",example = "2001-01-01 01:01:01")
    //@JsonProperty("valid_time")
    private Date validTime;
    @ApiModelProperty(value = "出库时间",example = "2001-01-01 01:01:01")
    //@JsonProperty("out_stock_time")
    private Date outStockTime;
    @ApiModelProperty(value = "发运时间",example = "2001-01-01 01:01:01")
    //@JsonProperty("delivery_time")
    private Date deliveryTime;
    @ApiModelProperty(value = "完成时间",example = "2001-01-01 01:01:01")
    //@JsonProperty("finish_time")
    private Date finishTime;
    @ApiModelProperty(value = "备注")
    //@JsonProperty("remark")
    private String remark;
    @ApiModelProperty(value = "0. 启用   1.禁用")
    //@JsonProperty("use_status")
    private Integer useStatus;
    @ApiModelProperty(value = "来源单号")
    //@JsonProperty("source_code")
    private String sourceCode;
    @ApiModelProperty(value = "来源类型")
    //@JsonProperty("source_type")
    private Integer sourceType;
    @ApiModelProperty(value = "创建人编码")
    //@JsonProperty("create_by_id")
    private String createById;
    @ApiModelProperty(value = "创建人名称")
    //@JsonProperty("create_by_name")
    private String createByName;
    @ApiModelProperty(value = "修改人编码")
    //@JsonProperty("update_by_id")
    private String updateById;
    @ApiModelProperty(value = "修改人名称")
    //@JsonProperty("update_by_name")
    private String updateByName;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    //@JsonProperty("update_time")
    private Date updateTime;
}