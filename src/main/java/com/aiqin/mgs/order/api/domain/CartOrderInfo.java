package com.aiqin.mgs.order.api.domain;

import com.aiqin.mgs.order.api.base.PagesRequest;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

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
