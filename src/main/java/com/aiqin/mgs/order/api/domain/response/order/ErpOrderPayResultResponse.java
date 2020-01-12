package com.aiqin.mgs.order.api.domain.response.order;

import com.aiqin.mgs.order.api.component.enums.pay.ErpPayStatusEnum;
import com.aiqin.mgs.order.api.domain.po.order.ErpOrderInfo;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单支付结果查询返回值
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/11/25 14:49
 */
@Data
@JsonInclude(JsonInclude.Include.ALWAYS)
public class ErpOrderPayResultResponse {

    @ApiModelProperty(value = "订单头信息 方便取字段使用")
    @JsonProperty("order_info")
    private ErpOrderInfo orderInfo;

    @ApiModelProperty(value = "订单id")
    @JsonProperty("order_id")
    private String orderId;

    @ApiModelProperty(value = "订单编码")
    @JsonProperty("order_code")
    private String orderCode;

    @ApiModelProperty(value = "实付金额")
    @JsonProperty("pay_money")
    private BigDecimal payMoney;

    @ApiModelProperty(value = "支付状态 0待支付 1支付中 2支付成功 3支付失败")
    @JsonProperty("pay_status")
    private Integer payStatus;

    @ApiModelProperty(value = "支付状态描述")
    @JsonProperty("pay_status_desc")
    private String payStatusDesc;

    @ApiModelProperty(value = "返还物流券金额")
    @JsonProperty("goods_coupon")
    private BigDecimal goodsCoupon;

    @ApiModelProperty(value = "支付流水号")
    @JsonProperty("pay_code")
    private String payCode;

    @ApiModelProperty(value = "支付开始时间")
    @JsonProperty("pay_start_time")
    private Date payStartTime;

    @ApiModelProperty(value = "支付完成时间")
    @JsonProperty("pay_end_time")
    private Date payEndTime;

    @ApiModelProperty(value = "收货人")
    @JsonProperty("receive_person")
    private String receivePerson;

    @ApiModelProperty(value = "收货人电话")
    @JsonProperty("receive_mobile")
    private String receiveMobile;

    @ApiModelProperty(value = "收货地址")
    @JsonProperty("receive_address")
    private String receiveAddress;

    public String getPayStatusDesc() {
        return ErpPayStatusEnum.getEnumDesc(payStatus);
    }
}
