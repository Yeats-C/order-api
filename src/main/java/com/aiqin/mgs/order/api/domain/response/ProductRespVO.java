package com.aiqin.mgs.order.api.domain.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Optional;

/**
 * Created by yangjun
 * 2018/11/14
 */
@Data
@ApiModel("商品详情VO")
public class ProductRespVO {

    @ApiModelProperty(value="分销机构id")
    @JsonProperty("distributor_id")
    private String distributorId;

    @JsonProperty("sku_code")
    @ApiModelProperty("sku code")
    private String skuCode;

    @ApiModelProperty("sku_name")
    @JsonProperty("sku_name")
    private String skuName ;

    @JsonProperty("bar_code")
    @ApiModelProperty("条形码")
    private String barCode;

    @JsonProperty("spu_code")
    @ApiModelProperty("spu code")
    private String spuCode;

    @ApiModelProperty("规格")
    private String spec;

    @ApiModelProperty("单位")
    private String unit;

    @ApiModelProperty("颜色code")
    @JsonProperty("color_code")
    private String colorCode;

    @ApiModelProperty("颜色")
    @JsonProperty("color_name")
    private String colorName;

    @ApiModelProperty("型号")
    @JsonProperty("model_number")
    private String modelNumber;

    @JsonProperty("product_id")
    @ApiModelProperty("商品id")
    private String productId;

    @JsonProperty("category_id")
    @ApiModelProperty("商品分类id")
    private String categoryId;

    @JsonProperty("category_name")
    @ApiModelProperty("商品分类id")
    private String categoryName;

    @JsonProperty("product_code")
    @ApiModelProperty("商品编码")
    private String productCode;

    @JsonProperty("product_status")
    @ApiModelProperty("商品状态")
    private Integer productStatus;

    @JsonProperty("product_name")
    @ApiModelProperty("商品名称")
    private String productName;

    @JsonProperty("show_name")
    @ApiModelProperty("列表名称")
    private String showName;

    @JsonProperty("sku_status")
    @ApiModelProperty("sku状态，0为启用，1为禁用")
    private Integer skuStatus;

    @JsonProperty("logo")
    @ApiModelProperty("列表图")
    private String logo;

    @JsonProperty("images")
    @ApiModelProperty("封面图")
    private String images;

    @JsonProperty("itro_images")
    @ApiModelProperty("详情图")
    private String itroImages;

    @ApiModelProperty("商品零售价")
    @JsonProperty("price")
    private Long price;

    @ApiModelProperty(value = "会员价格")
    @JsonProperty("member_price")
    private Long memberPrice;

    @JsonProperty("custom_sales")
    @ApiModelProperty("自定义销量")
    private Integer customSales;

    @JsonProperty("warning_status")
    @ApiModelProperty("预警状态，0为正常，1为预警")
    private Integer warningStatus;

    @JsonProperty("status")
    @ApiModelProperty("活动明细状态，0为启用，1为禁用")
    private Long status;

    @JsonProperty("amount")
    @ApiModelProperty("商品数量")
    private int amount;

    @JsonProperty("storage_type")
    @ApiModelProperty("仓库类型，0为门店自有仓，1为爱亲大仓")
    private int storageType;

    @JsonProperty("display_stock")
    @ApiModelProperty("陈列仓位库存")
    private int displayStock;

    @JsonProperty("available_stock")
    @ApiModelProperty("可用库存")
    private int availableStock;

    @JsonProperty("return_stock")
    @ApiModelProperty("退货仓位库存")
    private int returnStock;

    @JsonProperty("storage_stock")
    @ApiModelProperty("存储仓位库存")
    private int storageStock;

    @JsonProperty("lock_stock")
    @ApiModelProperty("锁库库存")
    private int lockStock;

    @JsonProperty("warning_stock")
    @ApiModelProperty("预警库存")
    private int warningStock;

    @JsonProperty("show_stock")
    @ApiModelProperty("展示库存")
    private int showStock;

    @JsonProperty("is_activity")
    @ApiModelProperty("是否限时折扣 1 是  0 否") // todo  是否参与活动
    private Integer isActivity;

    @JsonProperty("activity_type")
    @ApiModelProperty("活动类型 0:领券活动 1:促销活动 2：限时折扣活动 3:定金活动 4:妈妈班 5:抽奖活动 6:满减 7：满赠 8：套餐包 9 ：第N件特价'")
    private Integer activityType;

    @JsonProperty("is_coupon")
    @ApiModelProperty("商品是否有优惠券 1 是  0 否")
    private Integer isCoupon;

    @JsonProperty("discount")
    @ApiModelProperty("折扣")
    private Long discount;

    @JsonProperty("reduce")
    @ApiModelProperty("减价金额")
    private Long reduce;

    @JsonProperty("actual_price")
    @ApiModelProperty("活动实际价格")
    private Long actualPrice;

    @JsonProperty("product_avg_cost")
    @ApiModelProperty("商品平均成本")
    private Long productAvgCost;

    @JsonProperty("purchase_limit")
    @ApiModelProperty("是否限购，0为不限购，1为每人每种商品限购，2为每人每种前几件商品限购")
    private Integer purchaseLimit;

    @ApiModelProperty(value = "限购数量")
    @JsonProperty("limit_count")
    private Integer limitCount;



    @ApiModelProperty("是否预存订单，1：是 ，2 否")
    @JsonProperty("is_prestorage")
    private Integer isPrestorage=2;


    /**
     * --------------------------------------------
     */
    @ApiModelProperty("活动名称")
    @JsonProperty("activity_name")
    private String activityName;

    @ApiModelProperty("活动id")
    @JsonProperty("activity_id")
    private String activityId;

    @ApiModelProperty("活动编号")
    @JsonProperty("activity_num")
    private String activityNum;

    @JsonProperty("points")
    @ApiModelProperty("购买后赠送积分")
    private Integer points;

    @JsonProperty("package_id")
    @ApiModelProperty("套餐包id")
    private String packageId;


    @ApiModelProperty("商品属性code")
    @JsonProperty("product_property_code")
    private String productPropertyCode;

    @ApiModelProperty("商品属性名称")
    @JsonProperty("product_property_name")
    private String productPropertyName;

    @ApiModelProperty(value = "适用客户范围  1：所有客户  2： 仅会员")
    @JsonProperty("applicable_customers_range")
    private Integer applicableCustomersRange;


    @ApiModelProperty(value = "品牌类型id")
    @JsonProperty("brand_id")
    private String brandId;

    @ApiModelProperty(value = "品牌类型名称")
    @JsonProperty("brand_name")
    private String brandName;


    public Integer getAvailableStock() {
        return Optional.ofNullable(displayStock).orElse(0)-Optional.ofNullable(lockStock).orElse(0);
    }
}
