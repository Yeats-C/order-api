package com.aiqin.mgs.order.api.domain.request.cart;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("添加商品到购物车sku行")
public class ErpCartAddSkuItem {

    @ApiModelProperty(value = "商品数量 必填")
    @JsonProperty("amount")
    private Integer amount;

    @ApiModelProperty(value = "sku码 必填")
    @JsonProperty("sku_code")
    private String skuCode;

    @ApiModelProperty(value = "活动id")
    @JsonProperty("activity_id")
    private String activityId;

    @JsonProperty("warehouse_type_code")
    @ApiModelProperty( value = "传入库房编码:1:销售库，2:特卖库" )
    private String  warehouseTypeCode;

    @ApiModelProperty("批次编号")
    @JsonProperty(value = "batch_info_code")
    private String batchInfoCode;
}