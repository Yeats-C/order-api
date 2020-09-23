package com.aiqin.mgs.order.api.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author wanghao
 */
@Data
@ApiModel("订单销售统计响应实体")
public class OrderStatisticsRespVo {

    @ApiModelProperty(value = "总实付金额")
    private Long actualPaymentAmount = 0L;

    @ApiModelProperty(value = "总积分抵扣金额")
    private Long pointsDeductionAmount = 0L;

    @ApiModelProperty(value = "销售笔数")
    private Long saleTotal = 0L;

    @ApiModelProperty(value = "总退款金额")
    private Long returnAmount = 0L;

}
