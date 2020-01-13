package com.aiqin.mgs.order.api.domain.response.cart;

import com.aiqin.mgs.order.api.domain.CartOrderInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderConfirmResponse {

    /***门店地址*/
    private String storeAddress;

    /***门店联系人*/
    private String storeContacts;

    /***门店联系人电话*/
    private String storeContactsPhone;

    /***商品返回列表*/
    private List<CartOrderInfo> cartOrderInfos;

    /**订货金额合计*/
    private BigDecimal acountActualprice;

    /**应付金额总和*/
    private BigDecimal acountTotalprice;

    /**是否有A类商品 0否  1是*/
    @ApiModelProperty(value = "是否有A类商品 0否  1是")
    @JsonProperty("have_product_A")
    private Integer haveProductA;

    /**A类商品金额总和*/
    @ApiModelProperty(value = "商品id")
    @JsonProperty("price_product_A")
    private BigDecimal priceProductA;

}
