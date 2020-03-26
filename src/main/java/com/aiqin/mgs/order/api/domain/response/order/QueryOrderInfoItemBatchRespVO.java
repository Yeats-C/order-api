package com.aiqin.mgs.order.api.domain.response.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@ApiModel("订单批次信息")
@Data
public class QueryOrderInfoItemBatchRespVO {

    @ApiModelProperty("订单编码")
    private String orderCode;

    @ApiModelProperty("sku编号")
    private String skuCode;

    @ApiModelProperty("sku名称")
    private String skuName;

    @ApiModelProperty("批次号")
    private String batchCode;

    @ApiModelProperty("生产日期")
    private Date productDate;

    @ApiModelProperty("批次备注")
    private String batchRemark;

    @ApiModelProperty("数量")
    private Long productCount;

    @ApiModelProperty("实发数量")
    private Long actualProductCount;

}
