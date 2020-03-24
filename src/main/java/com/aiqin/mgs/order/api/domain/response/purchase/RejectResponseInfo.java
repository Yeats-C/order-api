package com.aiqin.mgs.order.api.domain.response.purchase;

import com.aiqin.mgs.order.api.domain.OperationLog;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class RejectResponseInfo {
    @ApiModelProperty(value = "")
    @JsonProperty("id")
    private Long id;

    @ApiModelProperty(value = "退供单id")
    @JsonProperty("reject_record_id")
    private String rejectRecordId;

    @ApiModelProperty(value = "退供单编码")
    @JsonProperty("reject_record_code")
    private String rejectRecordCode;

    @ApiModelProperty(value = "结算方式编码")
    @JsonProperty("settlement_method_code")
    private String settlementMethodCode;

    @ApiModelProperty(value = "结算方式名称")
    @JsonProperty("settlement_method_name")
    private String settlementMethodName;

    @ApiModelProperty(value = "负责人")
    @JsonProperty("charge_person")
    private String chargePerson;

    @ApiModelProperty(value = "联系人")
    @JsonProperty("contact_person")
    private String contactPerson;

    @ApiModelProperty(value = "联系人电话")
    @JsonProperty("contact_mobile")
    private String contactMobile;

    @ApiModelProperty(value = "收货区域 :省")
    @JsonProperty("province_id")
    private String provinceId;

    @ApiModelProperty(value = "省名称")
    @JsonProperty("province_name")
    private String provinceName;

    @ApiModelProperty(value = "市")
    @JsonProperty("city_id")
    private String cityId;

    @ApiModelProperty(value = "市名称")
    @JsonProperty("city_name")
    private String cityName;

    @ApiModelProperty(value = "县")
    @JsonProperty("district_id")
    private String districtId;

    @ApiModelProperty(value = "县名称")
    @JsonProperty("district_name")
    private String districtName;

    @ApiModelProperty(value = "收货地址")
    @JsonProperty("receive_address")
    private String receiveAddress;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "预计配送时间")
    @JsonProperty("pre_expect_time")
    private Date preExpectTime;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "有效期")
    @JsonProperty("valid_time")
    private Date validTime;

    @ApiModelProperty(value = "供应商code")
    @JsonProperty("supplier_code")
    private String supplierCode;

    @ApiModelProperty(value = "供应商名称")
    @JsonProperty("supplier_name")
    private String supplierName;

    @ApiModelProperty(value = "采购组编码")
    @JsonProperty("purchase_group_code")
    private String purchaseGroupCode;

    @ApiModelProperty(value = "采购组名称")
    @JsonProperty("purchase_group_name")
    private String purchaseGroupName;

    @ApiModelProperty(value = "退供单状态:1 进行中  2 完成")
    @JsonProperty("reject_record_status")
    private Integer rejectRecordStatus;

    @ApiModelProperty(value = "备注")
    @JsonProperty("remark")
    private String remark;

    @Size(max = 66,message = "运输信息说明长度小于66")
    @ApiModelProperty(value = "运输信息说明")
    @JsonProperty("transport_remark")
    private String transportRemark;

    @ApiModelProperty(value = "运输单图片")
    @JsonProperty("transport_url")
    private String transportUrl;

    @ApiModelProperty(value = "出库时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("out_stock_time")
    private Date outStockTime;

    @ApiModelProperty(value = "普通商品数量")
    @JsonProperty("product_count")
    private Integer productCount;

    @ApiModelProperty(value = "单品数量")
    @JsonProperty("total_count")
    private Integer totalCount;

    @ApiModelProperty(value = "商品含税金额")
    @JsonProperty("total_tax_amount")
    private BigDecimal totalTaxAmount;

    @ApiModelProperty(value = "实际商品含税金额")
    @JsonProperty("actual_total_tax_amount")
    private BigDecimal actualTotalTaxAmount;

    @ApiModelProperty(value = "实际最小单数数量")
    @JsonProperty("actual_total_count")
    private Integer actualTotalCount;

    @ApiModelProperty(value = "创建人id")
    @JsonProperty("create_by_id")
    private String createById;

    @ApiModelProperty(value = "创建人名称")
    @JsonProperty("create_by_name")
    private String createByName;

    @ApiModelProperty(value = "修改人id")
    @JsonProperty("update_by_id")
    private String updateById;

    @ApiModelProperty(value = "修改人名称")
    @JsonProperty("update_by_name")
    private String updateByName;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    @JsonProperty("create_time")
    private Date createTime;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "修改时间")
    @JsonProperty("update_time")
    private Date updateTime;

    @ApiModelProperty(value = "仓编码(物流中心编码)")
    @JsonProperty("transport_center_code")
    private String transportCenterCode;

    @ApiModelProperty(value = "仓名称(物流中心名称)")
    @JsonProperty("transport_center_name")
    private String transportCenterName;

    @ApiModelProperty(value = "库房编码")
    @JsonProperty("warehouse_code")
    private String warehouseCode;

    @ApiModelProperty(value = "库房名称")
    @JsonProperty("warehouse_name")
    private String warehouseName;

    @ApiModelProperty("创建人公司编码")
    @JsonProperty("company_code")
    private String companyCode;

    @ApiModelProperty("创建人公司名称")
    @JsonProperty("company_name")
    private String companyName;

    @ApiModelProperty("供应商评分编号")
    @JsonProperty("score_code")
    private String scoreCode;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "发运时间")
    @JsonProperty("delivery_time")
    private Date deliveryTime;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "完成时间")
    @JsonProperty("finish_time")
    private Date finishTime;

    @ApiModelProperty("批次商品列表")
    @JsonProperty("batch_list")
    private List<RejectRecordDetail> batchList;

    @ApiModelProperty("商品列表")
    @JsonProperty("product_list")
    private List<RejectRecordDetailResponse> productList;

    @ApiModelProperty("日志记录")
    @JsonProperty("log_list")
    private List<OperationLog> logList;

}
