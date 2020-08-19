package com.aiqin.mgs.order.api.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ProductSkuData {

    @ApiModelProperty(value = "sku编码")
    @JsonProperty("sku_code")
    private String skuCode;

    @ApiModelProperty(value = "sku名称")
    @JsonProperty("sku_name")
    private String skuName;

    @JsonProperty("warehouse_type_code")
    @ApiModelProperty( value = "传入库房编码:1:销售库，2:特卖库 0:全部" )
    private String  warehouseTypeCode;

    @ApiModelProperty("配送中心编码")
    @JsonProperty("transport_center_code")
    private String transportCenterCode;


}
