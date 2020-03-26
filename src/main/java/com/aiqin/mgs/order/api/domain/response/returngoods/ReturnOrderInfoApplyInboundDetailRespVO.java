package com.aiqin.mgs.order.api.domain.response.returngoods;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;


@Data
@ApiModel("批次列表返回vo")
public class ReturnOrderInfoApplyInboundDetailRespVO {

    @ApiModelProperty("sku编号")
    private String skuCode;

    @ApiModelProperty("sku名称")
    private String skuName;

    @ApiModelProperty("批次号")
    private String batchNumber;

    @ApiModelProperty("供应商名称")
    private String supplierName;

    @ApiModelProperty("供应商编号")
    private String supplierCode;

    @ApiModelProperty("退货数量")
    private Long num;

    @ApiModelProperty("实退数量")
    private Long praNum;

    @ApiModelProperty("生产日期")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date manufactureTime;

    @ApiModelProperty("批次备注")
    private String batchRemark;

    @ApiModelProperty("库位号")
    private String storeHouseCode;
}
