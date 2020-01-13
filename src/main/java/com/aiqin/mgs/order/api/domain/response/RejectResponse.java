package com.aiqin.mgs.order.api.domain.response;


import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@ApiModel("退供单单列表")
public class RejectResponse {
    @ApiModelProperty(value = "退供单号")
    @JsonProperty("reject_record_code")
    private String rejectRecordCode;

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

    @ApiModelProperty(value = "最小单位数量")
    @JsonProperty("total_count")
    private Long totalCount;

    @ApiModelProperty(value = "商品含税金额")
    @JsonProperty("total_tax_amount")
    private BigDecimal totalTaxAmount;

    @ApiModelProperty(value = "实际商品含税金额")
    @JsonProperty("actual_total_tax_amount")
    private BigDecimal actualTotalTaxAmount;

    @ApiModelProperty(value = "采购组名称")
    @JsonProperty("purchase_group_name")
    private String purchaseGroupName;

    @ApiModelProperty(value = "创建人名称")
    @JsonProperty("create_by_name")
    private String createByName;

    @ApiModelProperty(value = "修改人名称")
    @JsonProperty("update_by_name")
    private String updateByName;
    @ApiModelProperty(value = "创建时间",example = "2001-01-01 01:01:01")
    @JsonProperty("create_time")
    private Date createTime;
    @ApiModelProperty(value = "修改时间",example = "2001-01-01 01:01:01")
    @JsonProperty("update_time")
    private Date updateTime;
}
