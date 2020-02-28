package com.aiqin.mgs.order.api.domain.response;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel("门店id获取订单信息")
public class FirstReportOrderResponse {

    @ApiModelProperty(value = "订单编码")
    private String orderStoreCode;

    @ApiModelProperty(value = "订单类型编码 1直送 2配送 3辅采直送")
    private String orderTypeCode;

    @ApiModelProperty(value = "订单类别编码 2普通首单 3首单赠送 16新店货架")
    private String orderCategoryCode;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;
}
