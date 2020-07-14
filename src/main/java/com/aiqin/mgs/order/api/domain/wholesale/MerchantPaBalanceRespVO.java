package com.aiqin.mgs.order.api.domain.wholesale;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@ApiModel("加盟商平台账户余额响应VO")
public class MerchantPaBalanceRespVO implements Serializable {

    @ApiModelProperty("可用余额")
    private BigDecimal availableBalance;
    @ApiModelProperty("冻结余额")
    private BigDecimal frozenBalance;
    @ApiModelProperty("授信金额")
    private BigDecimal creditAmount;

}
