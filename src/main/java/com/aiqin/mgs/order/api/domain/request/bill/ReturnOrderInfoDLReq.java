package com.aiqin.mgs.order.api.domain.request.bill;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@ApiModel("退货商品主表")
public class ReturnOrderInfoDLReq {

    @ApiModelProperty(value = "主键id")
    private Long id;


    @ApiModelProperty(value = "退货单号")
    private String returnOrderCode;

    @ApiModelProperty(value = "实退商品数量")
    private Long actualProductCount;

    @ApiModelProperty(value = "退货人id")
    private String returnById;


    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date returnTime;

}