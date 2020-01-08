package com.aiqin.mgs.order.api.domain.request.bill;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@ApiModel("退货单商品表")
public class ReturnOrderDetailDLReq {

    @ApiModelProperty(value = "主键id")
    private Long id;

    @ApiModelProperty(value = "实退数量")
    private Long actualReturnProductCount;

    @ApiModelProperty(value = "sku编号")
    private String skuCode;

    @ApiModelProperty(value = "sku名称")
    private String skuName;

    @ApiModelProperty(value = "行号")
    private Long lineCode;

}