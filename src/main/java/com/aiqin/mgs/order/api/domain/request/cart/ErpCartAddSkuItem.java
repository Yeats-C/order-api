package com.aiqin.mgs.order.api.domain.request.cart;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

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

    @ApiModelProperty(value = "爱亲批发价")
    @JsonProperty("wholesale_price")
    private BigDecimal wholesalePrice;

    @ApiModelProperty(value = "仓库编码")
    @JsonProperty("warehouse_code")
    private String warehouseCode;

    @ApiModelProperty(value = "本品或赠品 0本品 1赠品")
    @JsonProperty("product_gift")
    private Integer productGift=0;
}
