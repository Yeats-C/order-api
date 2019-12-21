package com.aiqin.mgs.order.api.domain.request.cart;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("删除购物车商品请求参数")
public class DeleteCartProductRequest {

    /**门店id*/
    @ApiModelProperty(value = "门店ID")
    @JsonProperty("store_id")
    private String storeId;

    /**skuId*/
    @ApiModelProperty(value = "skuId")
    @JsonProperty("sku_id")
    private String skuId;

    /**删除勾选商品*/
    @ApiModelProperty(value = "勾选标记")
    @JsonProperty("line_check_status")
    private Integer lineCheckStatus;

}
