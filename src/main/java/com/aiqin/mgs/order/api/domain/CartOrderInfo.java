package com.aiqin.mgs.order.api.domain;

import com.aiqin.mgs.order.api.base.PagesRequest;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Date;

@ApiModel("购物车商品项")
public class CartOrderInfo extends PagesRequest {

    @ApiModelProperty(value = "商品id")
    @JsonProperty("product_id")
    private String productId="";

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

    @ApiModelProperty(value = "活动id")
    @JsonProperty("activity_id")
    private String activityId="";

    @ApiModelProperty(value = "活动名称")
    @JsonProperty("activity_name")
    private String activityName="";

    @ApiModelProperty(value = "购物车id")
    @JsonProperty("cart_id")
    private String cartId;

    @ApiModelProperty(value = "加盟商id")
    @JsonProperty("franchisee_id")
    private String franchiseeId;

    @ApiModelProperty(value = "门店id")
    @JsonProperty("store_id")
    private String storeId;

    @ApiModelProperty(value = "sku码")
    @JsonProperty("sku_id")
    private String skuId;

    @ApiModelProperty(value = "spu码")
    @JsonProperty("spu_id")
    private String spuId;

    @ApiModelProperty(value = "配送方式 1:配送 2:直送 3:货架 ")
    @JsonProperty("product_type")
    private Integer productType;


    public Integer getProductType() {
        return productType;
    }

    public void setProductType(Integer productType) {
        this.productType = productType;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getCartId() {
        return cartId;
    }

    public void setCartId(String cartId) {
        this.cartId = cartId;
    }

    public String getFranchiseeId() {
        return franchiseeId;
    }

    public void setFranchiseeId(String franchiseeId) {
        this.franchiseeId = franchiseeId;
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

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
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

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }
}
