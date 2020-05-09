package com.aiqin.mgs.order.api.domain.response.cart;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@ApiModel(value = "门店查询订单确认页面信息")
public class ErpStoreCartQueryResponse {

    @ApiModelProperty(value = "楼层列表 每一行是一个楼层")
    @JsonProperty("cart_group_list")
    private List<ErpCartGroupInfo> cartGroupList;

    @ApiModelProperty(value = "勾选商品活动价汇总，对应订单结算页面的订货金额合计")
    @JsonProperty("activity_amount_total")
    private BigDecimal activityAmountTotal;

    @ApiModelProperty(value = "活动优惠金额")
    @JsonProperty("activity_discount_amount")
    private BigDecimal activityDiscountAmount;

    @ApiModelProperty(value = "商品总数量")
    @JsonProperty("total_number")
    private Integer totalNumber;

    @ApiModelProperty(value = "所有勾选的商品中可使用A品券的商品活动均摊后金额汇总")
    @JsonProperty("top_total_price")
    private BigDecimal topTotalPrice;

    @ApiModelProperty(value = "门店地址")
    @JsonProperty("store_address")
    private String storeAddress;

    @ApiModelProperty(value = "门店联系人")
    @JsonProperty("store_contacts")
    private String storeContacts;

    @ApiModelProperty(value = "门店联系人电话")
    @JsonProperty("store_contacts_phone")
    private String storeContactsPhone;

    @ApiModelProperty(value = "门店Id")
    @JsonProperty("store_id")
    private String storeId;

    @ApiModelProperty(value = "订单类型 1直送 2配送")
    @JsonProperty("product_type")
    private Integer productType;

    @ApiModelProperty(value = "兑换赠品购物车商品列表")
    @JsonProperty("erp_cart_query_response")
    private ErpCartQueryResponse erpCartQueryResponse;

}
