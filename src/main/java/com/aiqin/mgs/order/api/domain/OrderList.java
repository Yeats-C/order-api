package com.aiqin.mgs.order.api.domain;

import com.aiqin.mgs.order.api.base.PagesRequest;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@ApiModel("订单主表")
@Data
public class OrderList extends PagesRequest {
    private String id;

    @ApiModelProperty(value = "订单code")
    @JsonProperty("order_code")
    private String orderCode;

    @ApiModelProperty(value = "订单类型(0:配送补货、1:直送补货、2:首单、3:首单赠送)")
    @JsonProperty("order_type")
    private Integer orderType;

    @ApiModelProperty(value = "订单状态")
    @JsonProperty("order_status")
    private String orderStatus;

    @ApiModelProperty(value = "支付状态")
    @JsonProperty("payment_status")
    private Integer paymentStatus;

    @ApiModelProperty(value = "门店id")
    @JsonProperty("store_id")
    private String storeId;

    @ApiModelProperty(value = "门店code")
    @JsonProperty("store_code")
    private String storeCode;

    @ApiModelProperty(value = "门店名称")
    @JsonProperty("store_name")
    private String storeName;

    @ApiModelProperty(value = "门店类型")
    @JsonProperty("store_type")
    private String storeType;

    @ApiModelProperty(value = "订单总额")
    @JsonProperty("total_orders")
    private Integer totalOrders;

    @ApiModelProperty(value = "实付金额")
    @JsonProperty("actual_amount_paid")
    private Integer actualAmountPaid;

    @ApiModelProperty(value = "活动金额")
    @JsonProperty("activity_amount")
    private Integer activityAmount;

    @ApiModelProperty(value = "优惠额度")
    @JsonProperty("preferential_quota")
    private Integer preferentialQuota;

    @ApiModelProperty(value = "物流减免比例")
    @JsonProperty("logistics_remission_ratio")
    private Integer logisticsRemissionRatio;

    @ApiModelProperty(value = "下单人")
    @JsonProperty("place_order_by")
    private String placeOrderBy;

    @ApiModelProperty(value = "下单时间")
    @JsonProperty("place_order_time")
    private Date placeOrderTime;

    @ApiModelProperty(value = "账户余额")
    @JsonProperty("account_balance")
    private Integer accountBalance;

    @ApiModelProperty(value = "授信额度")
    @JsonProperty("line_credit")
    private Integer lineCredit;

    @ApiModelProperty(value = "收货地址")
    @JsonProperty("receiving_address")
    private String receivingAddress;

    @ApiModelProperty(value = "到货时间")
    @JsonProperty("arrival_time")
    private Date arrivalTime;

    @ApiModelProperty(value = "收货人")
    @JsonProperty("consignee")
    private String consignee;

    @ApiModelProperty(value = "收货人电话")
    @JsonProperty("consignee_phone")
    private String consigneePhone;

    @ApiModelProperty(value = "所属公司code")
    @JsonProperty("company_code")
    private String companyCode;

    @ApiModelProperty(value = "公司名称")
    @JsonProperty("company_name")
    private String companyName;

    @ApiModelProperty(value = "创建人")
    @JsonProperty("create_by")
    private String createBy;

    @ApiModelProperty(value = "修改人")
    @JsonProperty("update_by")
    private String updateBy;



}