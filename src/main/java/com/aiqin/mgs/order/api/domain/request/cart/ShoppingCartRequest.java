package com.aiqin.mgs.order.api.domain.request.cart;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

public class ShoppingCartRequest {

    @ApiModelProperty(value = "商品id")
    @JsonProperty("product_id")
    private String productId;

    @ApiModelProperty(value = "商品名称")
    @JsonProperty("product_name")
    private String productName="";

    @ApiModelProperty(value = "商品数量")
    @JsonProperty("amount")
    private int amount;

    @ApiModelProperty(value = "logo图片")
    @JsonProperty("logo")
    private String logo="";

    @ApiModelProperty(value = "价格")
    @JsonProperty("price")
    private BigDecimal price;

    @ApiModelProperty(value = "商品颜色")
    @JsonProperty("color")
    private String color="";

    @ApiModelProperty(value = "商品规格")
    @JsonProperty("product_size")
    private String productSize="";

    @ApiModelProperty(value = "门店id")
    @JsonProperty("store_id")
    private String storeId;

    @ApiModelProperty(value = "sku码")
    @JsonProperty("sku_id")
    private String skuId;

    @ApiModelProperty(value = "spu码")
    @JsonProperty("spu_id")
    private String spuId;

    @ApiModelProperty(value = "配送方式 1:配送 2:直送")
    @JsonProperty("product_type")
    private Integer productType;

    public ShoppingCartRequest() {
        productId = "";
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getProductSize() {
        return productSize;
    }

    public void setProductSize(String productSize) {
        this.productSize = productSize;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getSkuId() {
        return skuId;
    }

    public void setSkuId(String skuId) {
        this.skuId = skuId;
    }

    public String getSpuId() {
        return spuId;
    }

    public void setSpuId(String spuId) {
        this.spuId = spuId;
    }

    public Integer getProductType() {
        return productType;
    }

    public void setProductType(Integer productType) {
        this.productType = productType;
    }
}
