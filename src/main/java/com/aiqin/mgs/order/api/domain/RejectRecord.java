package com.aiqin.mgs.order.api.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    @ApiModelProperty(value = "业务id")
    private String rejectRecordId;
    @ApiModelProperty(value = "退供单号")
    private String rejectRecordCode;
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
    @ApiModelProperty(value = "退供单状态")
    private Integer rejectRecordStatus;
    @ApiModelProperty(value = "最小单位数量")
    private Long totalCount;
    @ApiModelProperty(value = "商品含税金额")
    private BigDecimal totalTaxAmount;
    @ApiModelProperty(value = "实际最小单数数量")
    private Long actualTotalCount;
    @ApiModelProperty(value = "实际商品含税金额")
    private BigDecimal actualTotalTaxAmount;
    @ApiModelProperty(value = "负责人")
    private String chargePerson;
    @ApiModelProperty(value = "联系人")
    private String contactPerson;
    @ApiModelProperty(value = "联系人电话")
    private String contactMobile;
    @ApiModelProperty(value = "收货区域 :省编码")
    private String provinceId;
    @ApiModelProperty(value = "收货区域 :省")
    private String provinceName;
    @ApiModelProperty(value = "收货区域 :市编码")
    private String cityId;
    @ApiModelProperty(value = "收货区域 :市")
    private String cityName;
    @ApiModelProperty(value = "收货区域 :区/县")
    private String districtId;
    @ApiModelProperty(value = "收货区域 :区/县")
    private String districtName;
    @ApiModelProperty(value = "收货地址")
    private String receiveAddress;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date preExpectTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date validTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date outStockTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date deliveryTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date finishTime;
    @ApiModelProperty(value = "备注")
    private String remark;
    @ApiModelProperty(value = "0. 启用   1.禁用")
    private Integer useStatus;
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