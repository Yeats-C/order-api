package com.aiqin.mgs.order.api.domain.request.cart;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("添加商品到购物车请求参数")
public class ErpCartAddRequest {

    @ApiModelProperty(value = "门店Id")
    @JsonProperty("store_id")
    private String storeId;

    @ApiModelProperty(value = "订单类型 1直送 2配送")
    @JsonProperty("product_type")
    private Integer productType;

    @ApiModelProperty(value = "spu编码")
    @JsonProperty("spu_code")
    private String spuCode;

    @ApiModelProperty(value = "sku列表")
    @JsonProperty("products")
    private List<ErpCartAddSkuItem> products;

    @ApiModelProperty(value = "创建来源 1、爱掌柜 2、erp")
    @JsonProperty("create_source")
    private String createSource;

    @ApiModelProperty(value = "是否是首单赠送 1 是 0 否")
    @JsonProperty("first_order_gift")
    private Integer firstOrderGift;

}
