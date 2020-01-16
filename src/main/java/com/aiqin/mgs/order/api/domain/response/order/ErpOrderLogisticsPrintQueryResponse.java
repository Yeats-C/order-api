package com.aiqin.mgs.order.api.domain.response.order;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty("store_name")
    private String storeName;

    @ApiModelProperty(value = "订单编号")
    @JsonProperty("order_code")
    private String orderCode;

    @ApiModelProperty(value = "物流公司编码")
    @JsonProperty("logistic_centre_code")
    private String logisticCentreCode;

    @ApiModelProperty(value = "物流公司名称")
    @JsonProperty("logistic_centre_name")
    private String logisticCentreName;

    @ApiModelProperty(value = "物流单号")
    @JsonProperty("logistic_code")
    private String logisticCode;

    @ApiModelProperty(value = "物流费用（元）")
    @JsonProperty("logistic_fee")
    private BigDecimal logisticFee;

    @ApiModelProperty(value = "物流券支付金额（元）")
    @JsonProperty("coupon_pay_fee")
    private BigDecimal couponPayFee;

    @ApiModelProperty(value = "余额支付金额（元）")
    @JsonProperty("balance_pay_fee")
    private BigDecimal balancePayFee;

    @ApiModelProperty(value = "支付流水号")
    @JsonProperty("pay_code")
    private String payCode;

    @ApiModelProperty(value = "支付完成时间")
    @JsonProperty("pay_end_time")
    private Date payEndTime;

    @ApiModelProperty(value = "支付人姓名")
    @JsonProperty("pay_user")
    private String payUser;

}
