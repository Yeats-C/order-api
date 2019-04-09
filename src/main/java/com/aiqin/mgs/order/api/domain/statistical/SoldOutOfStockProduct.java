package com.aiqin.mgs.order.api.domain.statistical;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Createed by sunx on 2019/4/8.<br/>
 */
@Data
@ApiModel("畅缺商品信息")
public class SoldOutOfStockProduct {

    @ApiModelProperty("sku编码")
    @JsonProperty("sku_code")
    private String skuCode;

    @ApiModelProperty("商品名称")
    @JsonProperty("product_name")
    private String productName;

    @ApiModelProperty("logo图")
    private String logo;

    @ApiModelProperty("14天销量")
    private Integer sales;

    @ApiModelProperty("前一天的库存")
    private Integer stock;
}
