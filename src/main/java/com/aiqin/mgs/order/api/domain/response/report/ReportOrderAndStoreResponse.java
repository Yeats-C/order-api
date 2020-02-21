package com.aiqin.mgs.order.api.domain.response.report;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * description: ReportOrderAndStoreResponse
 * date: 2020/2/20 19:44
 * author: hantao
 * version: 1.0
 */
@Data
@ApiModel("订单门店关系实体类")
public class ReportOrderAndStoreResponse implements Serializable {

    @ApiModelProperty(value = "订单编码")
    private String orderStoreCode;

    @ApiModelProperty(value = "门店编码")
    private String storeCode;

}
