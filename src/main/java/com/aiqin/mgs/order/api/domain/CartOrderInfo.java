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
    private Integer amount;

    /***商品最大订货数量*/
    @ApiModelProperty(value = "商品最大订货数量")
    @JsonProperty("max_number")
    private Integer maxNumber;

    /***最小订货倍数*/
    @ApiModelProperty(value = "最小订货倍数")
    @JsonProperty("order_multiple")
    private Integer order_multiple;

    /***库存数量*/
    @ApiModelProperty(value = "库存数量")
    @JsonProperty("stock_num")
    private int stockNum;

    /***生产日期*/
    @ApiModelProperty(value = "生产日期")
    @JsonProperty("production_date")
    private Date productionDate;

    /***商品标签名称*/
    @ApiModelProperty(value = "商品标签名称")
    @JsonProperty("label_name")
    private String labelName;

    /***商品标签名称*/
    @ApiModelProperty(value = "商品标签编码")
    @JsonProperty("label_code")
    private String labelCode;

    /***商品属性名称*/
    @ApiModelProperty(value = "商品属性名称")
    @JsonProperty("product_property_name")
    private String productPropertyName;

    /***商品属性编码*/
    @ApiModelProperty(value = "商品属性编码")
    @JsonProperty("product_property_code")
    private String productPropertyCode;

    /***商品品牌名称*/
    @ApiModelProperty(value = "商品品牌名称")
    @JsonProperty("product_brand_name")
    private String productBrandName;

    /***商品品牌编码*/
    @ApiModelProperty(value = "商品品牌编码")
    @JsonProperty("product_brand_code")
    private String productBrandCode;

    /***商品品类名称*/
    @ApiModelProperty(value = "商品品类名称")
    @JsonProperty("product_category_name")
    private String productCategoryName;

    /***商品品类编码*/
    @ApiModelProperty(value = "商品品类编码")
    @JsonProperty("product_category_code")
    private String productCategoryCode;


    /***商品logo图片*/
    @ApiModelProperty(value = "商品logo图片")
    @JsonProperty("logo")
    private String logo="";

    /***商品原价*/
    @ApiModelProperty(value = "商品原价（分销价）")
    @JsonProperty("price")
    private BigDecimal price;

    /***活动价*/
    @ApiModelProperty(value = "活动价")
    @JsonProperty("activity_price")
    private BigDecimal activityPrice;

    /***行原价汇总（分销价汇总）*/
    @ApiModelProperty(value = "行原价汇总（分销价汇总）")
    @JsonProperty("line_amount_total")
    private BigDecimal lineAmountTotal;

    /***行活动价汇总*/
    @ApiModelProperty(value = "行活动价汇总")
    @JsonProperty("line_activity_amount_total")
    private BigDecimal lineActivityAmountTotal;

    /***行活动优惠的金额*/
    @ApiModelProperty(value = "行活动优惠的金额，行根据活动使该行减少的金额，前端不显示")
    @JsonProperty("line_activity_discount_total")
    private BigDecimal lineActivityDiscountTotal;

    /***行减去活动优惠之后分摊的金额*/
    @ApiModelProperty(value = "计算了活动分摊之后的金额，前端不显示")
    @JsonProperty("line_amount_after_activity")
    private BigDecimal lineAmountAfterActivity;

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
    @JsonProperty("sku_code")
    private String skuCode;

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
    @ApiModelProperty(value = "skuCodes集合")
    @JsonProperty("sku_codes")
    private List<String> skuCodes;

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
    @ApiModelProperty(value = "商品添加来源 1:门店 2:erp")
    @JsonProperty("create_source")
    private String createSource;

    /***本品或者赠品 0:本品 1:赠品*/
    @ApiModelProperty(value = "本品或者赠品 0:本品 1:赠品")
    @JsonProperty("product_gift")
    private Integer productGift;

    /***赠品关联本品行cart_id*/
    @ApiModelProperty(value = "赠品关联本品行cart_id")
    @JsonProperty("gift_parent_cart_id")
    private String giftParentCartId;

    /***行是否选中 1:true 0:false*/
    @ApiModelProperty(value = "行是否选中 1:是 0:否")
    @JsonProperty("line_check_status")
    private Integer lineCheckStatus;


    /***爱亲销售价*/
    @ApiModelProperty(value = "爱亲销售价")
    @JsonProperty("price_tax2")
    private BigDecimal priceTax2;

    /***爱亲分销价*/
    @ApiModelProperty(value = "爱亲分销价")
    @JsonProperty("price_tax")
    private BigDecimal priceTax;

    /***创建者id*/
    @ApiModelProperty(value = "创建者id")
    @JsonProperty("create_by_id")
    private String createById;

    /***创建者名称*/
    @ApiModelProperty(value = "创建者名称")
    @JsonProperty("create_by_name")
    private String createByName;

    /****颜色名称--爱亲供应链字段*/
    @ApiModelProperty(value = "颜色名称")
    @JsonProperty("color_name")
    private String colorName;

    /****型号--爱亲供应链字段*/
    @ApiModelProperty(value = "型号")
    @JsonProperty("model_number")
    private String modelNumber;

    /**所属商品编码--爱亲供应链字段*/
    @ApiModelProperty(value = "所属商品编码")
    @JsonProperty("product_code")
    private String productCode;

     /**交易倍数--爱亲供应链字段*/
    @ApiModelProperty(value = "交易倍数")
    @JsonProperty("zero_removalCoefficient")
    private Integer zeroRemovalCoefficient;

    /**sku名称--爱亲供应链字段*/
    @ApiModelProperty(value = "sku名称")
    @JsonProperty("sku_name")
    private String skuName;

    /**规格--爱亲供应链字段*/
    @ApiModelProperty(value = "规格")
    @JsonProperty("spec")
    private String spec;

    @ApiModelProperty(value = "本商品待选择活动列表")
    @JsonProperty("activity_list")
    private List<Activity> activityList;

    @ApiModelProperty(value = "本商品行附带的赠品行")
    @JsonProperty("gift_list")
    private List<CartOrderInfo> giftList;



}
