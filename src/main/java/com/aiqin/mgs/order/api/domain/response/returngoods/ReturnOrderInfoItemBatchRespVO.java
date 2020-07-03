package com.aiqin.mgs.order.api.domain.response.returngoods;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel("商品批次信息")
public class ReturnOrderInfoItemBatchRespVO {

    @ApiModelProperty("sku编号")
    @JsonProperty("sku_code")
    private String skuCode;

    @ApiModelProperty("sku名称")
    @JsonProperty("sku_name")
    private String skuName;

    @ApiModelProperty("行号")
    @JsonProperty("line_code")
    private String lineCode;

    @ApiModelProperty("批次号【展示字段】")
    @JsonProperty("batch_code")
    private String batchCode;

    @ApiModelProperty("批次编码【供应链唯一标识】")
    @JsonProperty("batch_info_code")
    private String batchInfoCode;

    @ApiModelProperty("生产日期")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("product_date")
    private Date productDate;

    @ApiModelProperty("批次备注")
    @JsonProperty("batch_remark")
    private String batchRemark;

    @ApiModelProperty("供应商编码")
    @JsonProperty("supplier_code")
    private String supplierCode;

    @ApiModelProperty("供应商名称")
    @JsonProperty("supplier_name")
    private String supplierName;

    @ApiModelProperty("订单数量")
    @JsonProperty("return_product_count")
    private String returnProductCount;

    @ApiModelProperty("实发数量")
    @JsonProperty("actual_return_product_count")
    private String actualReturnProductCount;

    @ApiModelProperty("退货数量")
    @JsonProperty("return_total_count")
    private String returnTotalCount;

    @ApiModelProperty("库位号")
    @JsonProperty("location_code")
    private String locationCode;

    @ApiModelProperty("锁定类型")
    @JsonProperty("lock_type")
    private String lockType;

}
