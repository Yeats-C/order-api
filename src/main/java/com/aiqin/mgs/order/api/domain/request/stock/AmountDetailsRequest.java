package com.aiqin.mgs.order.api.domain.request.stock;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author wanghao
 */
@Data
@ApiModel("收银员报表收款退款请求实体")
public class AmountDetailsRequest {

    @ApiModelProperty("收银员id")
    private String cashierId;

    private String startTime;

    private String endTime;

    @ApiModelProperty("结算方式 3:到店支付-现金，4:到店支付-微信，5:到店支付-支付宝，6:到店支付-银行卡 7:积分")
    private Integer settlementType;
}
