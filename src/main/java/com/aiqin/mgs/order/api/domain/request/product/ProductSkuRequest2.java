package com.aiqin.mgs.order.api.domain.request.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * Created by 爱亲 on 2018/11/22.
 */
@ApiModel("商品skucode传参")
@Data
public class ProductSkuRequest2 {
    @ApiModelProperty(value = "sku编码")
    @JsonProperty("sku_code")
    private String skuCode;


    @JsonProperty("warehouse_type_code")
    @ApiModelProperty( value = "传入库房编码:1:销售库，2:特卖库" )
    private String  warehouseTypeCode="1";


    @ApiModelProperty("批次编号")
    @JsonProperty(value = "batch_info_code")
    private String batchInfoCode;

    @ApiModelProperty(value = "库房编码集合code")
    @JsonProperty("warehouse_codes")
    List<String> warehouseCodes;



}