package com.aiqin.mgs.order.api.domain.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("封装-统计商品在各个渠道的订单数")
@Data
public class ProductStoreResponse {

    @ApiModelProperty("skucode编码")
    @JsonProperty("code")
    private String code;

    @ApiModelProperty("销量")
    @JsonProperty("amount")
    private Integer amount;

    @JsonProperty("product_salability")
    @ApiModelProperty("畅销度")
    private Double productSalability;

}
