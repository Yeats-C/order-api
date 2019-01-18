package com.aiqin.mgs.order.api.domain.request.orderList;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * DiscountAmountInfo
 *
 * @author zhangtao
 * @createTime 2019-01-18
 * @description
 */
@Data
public class DiscountAmountInfo {
    @ApiModelProperty("优惠额编号")
    private String code;

    @ApiModelProperty("优惠额抵扣金额")
    private String amount;
}
