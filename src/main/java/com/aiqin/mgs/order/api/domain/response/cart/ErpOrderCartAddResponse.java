package com.aiqin.mgs.order.api.domain.response.cart;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value = "购物车添加商品成功时附带返回信息")
public class ErpOrderCartAddResponse {

    @ApiModelProperty(value = "是否有库存不足10个的商品 0无 1有")
    @JsonProperty("has_low_stock_product")
    private Integer hasLowStockProduct;

    @ApiModelProperty(value = "库存不足10个的商品列表")
    @JsonProperty("sku_stock_list")
    private List<ErpCartAddItemResponse> lowStockProductList;
}
