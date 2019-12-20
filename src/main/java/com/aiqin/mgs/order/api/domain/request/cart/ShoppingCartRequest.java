package com.aiqin.mgs.order.api.domain.request.cart;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("购物车请求参数")
public class ShoppingCartRequest {

      /***商品id*/
    @ApiModelProperty(value = "商品id")
    @JsonProperty("product_id")
    private String productId;

    /**商品集合*/
    @ApiModelProperty(value = "商品列表")
    @JsonProperty("products")
    private List<Product> products;

    /***门店id*/
    @ApiModelProperty(value = "门店Id")
    @JsonProperty("store_id")
    private String storeId;

    /***创建来源*/
    @ApiModelProperty(value = "创建来源")
    @JsonProperty("create_source")
    private String createSource;


}
