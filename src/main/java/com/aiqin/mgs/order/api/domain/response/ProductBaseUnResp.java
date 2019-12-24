package com.aiqin.mgs.order.api.domain.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel("商品概览页畅销滞销返回值")
public class ProductBaseUnResp {

    @ApiModelProperty(value = "sku编码")
    @JsonProperty("sku_code")
    private String skuCode;

    @ApiModelProperty(value = "sku名称")
    @JsonProperty("sku_name")
    private String skuName;

    @ApiModelProperty(value = "畅销度")
    @JsonProperty("saleout_dgree")
    private BigDecimal saleoutDgree;

    @ApiModelProperty(value = "毛利额")
    @JsonProperty("sale_margin")
    private BigDecimal saleMargin;

    @ApiModelProperty(value = "颜色")
    @JsonProperty("color_name")
    private String colorName;

    @ApiModelProperty(value = "规格")
    @JsonProperty("spec")
    private String spec;

    @ApiModelProperty(value = "型号")
    @JsonProperty("model_number")
    private String modelNumber;


}
