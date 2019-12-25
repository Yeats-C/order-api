package com.aiqin.mgs.order.api.domain.request.order;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.io.Serializable;
import java.util.Date;

/**
 * @Author:zfy
 * @Date:2019/12/23
 * @Content: tob支付请求参数
 */
@Data
public class PayRequest implements Serializable {


    private static final long serialVersionUID = -2797820355522762586L;

    @ApiModelProperty("订单号")
    @JsonProperty("order_no")
    @NotEmpty(message = "订单号不能为空")
    private String orderNo;

    @ApiModelProperty("订单金额-以分为单位的正整数")
    @JsonProperty("order_amount")
    @NotNull(message = "订单金额不能为空")
    private Long orderAmount;

    @ApiModelProperty("订单手续费-如无传0")
    @JsonProperty("fee")
    @NotNull(message = "订单手续费不能为空如无传0")
    private Long fee;

    @ApiModelProperty("下单时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("order_time")
    @PastOrPresent(message = "下单时间不能为空且为过去某个或现在的日期")
    private Date orderTime;

    @ApiModelProperty("支付方式 -10 tob余额支付 0-在线微信 1-在线支付")
    @JsonProperty("pay_type")
    @NotNull(message = "支付类型不能为空")
    private Integer payType;


    @JsonProperty("order_source")
    @ApiModelProperty("订单来源（0:pos 1：微商城  2：全部  3：web ）")
    @NotNull(message = "订单来源不能为空")
    private Integer orderSource;

    @ApiModelProperty("创建人编号 ")
    @JsonProperty("create_by")
    @NotEmpty(message = "创建人编号不能为空")
    private String createBy;

    @ApiModelProperty("创建人名称 ")
    @JsonProperty("update_by")
    @NotEmpty(message = "创建人名称不能为空")
    private String createName;


    @ApiModelProperty(value = "支付来源-TOB_PAY(7,配送订货-toB配送订单支付), 11,直送订货-toB直送订单支付 24,TOB-物流支付(包含所有订单状态下的支付)")
    @JsonProperty("pay_origin_type")
    @NotNull(message = "支付来源不能为空")
    private Integer payOriginType;


    @ApiModelProperty("1:充值；2：消费 3转账 4退款")
    @JsonProperty("order_type")
    @NotNull(message = "类型不能为空")
    private Integer orderType;

    @ApiModelProperty(value = "加盟商id")
    @JsonProperty("franchisee_id")
    @NotEmpty(message = "加盟商id不能为空")
    private String franchiseeId;

    @ApiModelProperty("门店名称")
    @JsonProperty("store_name")
    @NotEmpty(message = "门店名称不能为空")
    private String storeName;

    @ApiModelProperty("门店id")
    @JsonProperty("store_id")
    @NotEmpty(message = "门店id不能为空")
    private String storeId;

    @ApiModelProperty("交易类型：STORE_WITHDRAWAL 门店提现 " +
            "STORE_ORDER 门店订货 ACCOUNT_FREEZING 账户冻结 " +
            "FINANCIAL_TRANSFER_OUT 财务转出 " +
            "FINANCIAL_CREDIT_CANCELLATION 财务取消授信 " +
            "STORE_RECHARGE 门店充值 RETURN_REFUND 退货退款 " +
            "REFUND_ONLY 仅退款 STORE_CANCEL_ORDER 门店取消订单 " +
            "DELIVER_GOODS_DEDUCT 发货冲减 OUT_OF_STOCK_CANCELLATION 缺货取消 " +
            "ACCOUNT_UNFREEZING 账户解冻 FINANCIAL_TRANSFER_IN 财务转入 " +
            "FINANCIAL_CREDIT 财务授信 LOGISTICS_PAYMENT 物流费支付")
    private String transactionType;

    @ApiModelProperty("订单类型 14配送tob 2直送tob")
    @JsonProperty("pay_order_type")
    @NotNull(message = "订单类型不能为空")
    private Integer payOrderType;

    @ApiModelProperty("回调地址")
    @JsonProperty("back_url")
    private String backUrl;

}
