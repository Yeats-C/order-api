package com.aiqin.mgs.order.api.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@ApiModel("购物车商品项")
@Data
public class CartOrderInfo {

    /**商品ID*/
    @ApiModelProperty(value = "商品id")
    @JsonProperty("product_id")
    private String productId="";

    /***商品名称*/
    @ApiModelProperty(value = "商品名称")
    @JsonProperty("product_name")
    private String productName="";

    /***商品数量*/
    @ApiModelProperty(value = "商品数量")
    @JsonProperty("amount")
    private int amount;

    /***商品logo图片*/
    @ApiModelProperty(value = "商品logo图片")
    @JsonProperty("logo")
    private String logo="";

    /***商品原价*/
    @ApiModelProperty(value = "商品原价")
    @JsonProperty("price")
    private BigDecimal price;

    /**实付金额总和*/
    @ApiModelProperty(value = "实付金额总和")
    @JsonProperty("account_actual_price")
    private BigDecimal accountActualPrice;

    /**应付金额总和*/
    @ApiModelProperty(value = "应付金额总和")
    @JsonProperty("account_total_price")
    private BigDecimal accountTotalPrice;

    /***商品颜色*/
    @ApiModelProperty(value = "商品颜色")
    @JsonProperty("color")
    private String color="";

    /***商品规格*/
    @ApiModelProperty(value = "商品规格")
    @JsonProperty("product_size")
    private String productSize="";

    /***活动id*/
    @ApiModelProperty(value = "活动id")
    @JsonProperty("activity_id")
    private String activityId="";

    /***活动名称*/
    @ApiModelProperty(value = "活动名称")
    @JsonProperty("activity_name")
    private String activityName="";

    /***购物车id*/
    @ApiModelProperty(value = "购物车id")
    @JsonProperty("cart_id")
    private String cartId;

    /***加盟商id*/
    @ApiModelProperty(value = "加盟商id")
    @JsonProperty("franchisee_id")
    private String franchiseeId;

    /***门店Id*/
    @ApiModelProperty(value = "门店Id")
    @JsonProperty("store_id")
    private String storeId;

    /***sku码*/
    @ApiModelProperty(value = "sku码")
    @JsonProperty("sku_id")
    private String skuId;

    /***spu码*/
    @ApiModelProperty(value = "spu码")
    @JsonProperty("spu_id")
    private String spuId;

    /***创建时间*/
    @ApiModelProperty(value = "创建时间")
    @JsonProperty("create_time")
    private Date createTime;

    /***更新时间*/
    @ApiModelProperty(value = "更新时间")
    @JsonProperty("update_time")
    private Date updateTime;

    /***配送方式 1:配送 2:直送 3:货架*/
    @ApiModelProperty(value = "商品类型")
    @JsonProperty("product_type")
    private Integer productType;

    /***skuId集合*/
    @ApiModelProperty(value = "skuId集合")
    @JsonProperty("sku_ids")
    private List<String> skuIds;

    /***门店地址*/
    @ApiModelProperty(value = "门店地址")
    @JsonProperty("address")
    private String address;

    /***门店联系人*/
    @ApiModelProperty(value = "门店联系人")
    @JsonProperty("contacts")
    private String contacts;

    /***门店联系人电话*/
    @ApiModelProperty(value = "门店联系人电话")
    @JsonProperty("contacts_phone")
    private String contactsPhone;

    /***商品添加来源 1:门店 2:erp*/
    @ApiModelProperty(value = "商品添加来源")
    @JsonProperty("create_source")
    private String createSource;

    /***本品或者赠品 1:本品 2:赠品*/
    @ApiModelProperty(value = "本品或者赠品")
    @JsonProperty("product_gift")
    private Integer productGift;

    /***赠品关联本品行cart_id*/
    @ApiModelProperty(value = "赠品关联本品行cart_id")
    @JsonProperty("gift_parent_cart_id")
    private String giftParentCartId;

    /***行是否选中 1:true 0:false*/
    @ApiModelProperty(value = "行是否选中")
    @JsonProperty("line_check_status")
    private Integer lineCheckStatus;

}
