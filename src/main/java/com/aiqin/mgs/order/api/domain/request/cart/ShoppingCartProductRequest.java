package com.aiqin.mgs.order.api.domain.request.cart;

import com.aiqin.mgs.order.api.domain.request.product.ProductSkuRequest2;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("购物车-->查询商品详情请求参数")
public class ShoppingCartProductRequest {


    @ApiModelProperty(value = "商品sku编码集合")
    @JsonProperty("sku_codes")
    private List<String> skuCodes;

    @ApiModelProperty(value = "市code")
    @JsonProperty("city_code")
    private String cityCode;

    @ApiModelProperty(value = "公司编码")
    @JsonProperty("company_code")
    private String companyCode;

    @ApiModelProperty(value = "省code")
    @JsonProperty("province_code")
    private String provinceCode;

    @ApiModelProperty(value = "商品集合")
    @JsonProperty("product_sku_request2_list")
    private List<ProductSkuRequest2> productSkuRequest2List;



}
