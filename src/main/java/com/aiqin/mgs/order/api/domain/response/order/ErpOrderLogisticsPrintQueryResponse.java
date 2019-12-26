package com.aiqin.mgs.order.api.domain.response.order;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 物流费用支付凭证打印数据
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/12/10 16:40
 */
@Data
@JsonInclude(JsonInclude.Include.ALWAYS)
public class ErpOrderLogisticsPrintQueryResponse {

    @ApiModelProperty(value = "门店名称")
    private String storeName;
    @ApiModelProperty(value = "订单编号")
    private String orderCode;
    @ApiModelProperty(value = "物流公司编码")
    private String logisticCentreCode;
    @ApiModelProperty(value = "物流公司名称")
    private String logisticCentreName;
    @ApiModelProperty(value = "物流单号")
    private String logisticCode;
    @ApiModelProperty(value = "物流费用")
    private BigDecimal logisticFee;
    @ApiModelProperty(value = "物流券支付金额")
    private BigDecimal couponPayFee;
    @ApiModelProperty(value = "余额支付金额")
    private BigDecimal balancePayFee;
    @ApiModelProperty(value = "支付流水号")
    private String payCode;
    @ApiModelProperty(value = "支付完成时间")
    private Date payEndTime;
    @ApiModelProperty(value = "支付人姓名")
    private String payUser;

}
