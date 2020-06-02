package com.aiqin.mgs.order.api.domain.po.cart;

import com.aiqin.mgs.order.api.component.enums.cart.ErpCartLineStatusEnum;
import com.aiqin.mgs.order.api.domain.Activity;
import com.aiqin.mgs.order.api.domain.TagInfo;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 购物车实体
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2020/3/10 10:12
 */
@Data
@JsonInclude(JsonInclude.Include.ALWAYS)
public class ErpOrderCartInfo {

    @ApiModelProperty(value = "主键")
    @JsonProperty("id")
    private Long id;

    @ApiModelProperty(value = "购物车id，行唯一标识")
    @JsonProperty("cart_id")
    private String cartId;

    @ApiModelProperty(value = "门店id")
    @JsonProperty("store_id")
    private String storeId;

    @ApiModelProperty(value = "sku编码")
    @JsonProperty("sku_code")
    private String skuCode;

    @ApiModelProperty(value = "sku名称")
    @JsonProperty("sku_name")
    private String skuName;

    @ApiModelProperty(value = "spu编码")
    @JsonProperty("spu_code")
    private String spuCode;

    @ApiModelProperty(value = "spu名称")
    @JsonProperty("spu_name")
    private String spuName;

    @ApiModelProperty(value = "活动id")
    @JsonProperty("activity_id")
    private String activityId;

    @ApiModelProperty(value = "商品数量")
    @JsonProperty("amount")
    private Integer amount;

    @ApiModelProperty(value = "商品原价（分销价）")
    @JsonProperty("price")
    private BigDecimal price;

    @ApiModelProperty(value = "商品图片地址")
    @JsonProperty("logo")
    private String logo;

    @ApiModelProperty(value = "商品颜色")
    @JsonProperty("color")
    private String color;

    @ApiModelProperty(value = "商品规格")
    @JsonProperty("product_size")
    private String productSize;

    @ApiModelProperty(value = "创建时间")
    @JsonProperty("create_time")
    private Date createTime;

    @ApiModelProperty(value = "创建人编码")
    @JsonProperty("create_by_id")
    private String createById;

    @ApiModelProperty(value = "创建人名称")
    @JsonProperty("create_by_name")
    private String createByName;

    @ApiModelProperty(value = "最近更新时间")
    @JsonProperty("update_time")
    private Date updateTime;

    @ApiModelProperty(value = "最近更新人编码")
    @JsonProperty("update_by_id")
    private String updateById;

    @ApiModelProperty(value = "最近更新人名称")
    @JsonProperty("update_by_name")
    private String updateByName;

    @ApiModelProperty(value = "商品类型 1直送 2配送")
    @JsonProperty("product_type")
    private Integer productType;

    @ApiModelProperty(value = "商品添加来源 1门店 2erp")
    @JsonProperty("create_source")
    private String createSource;

    @ApiModelProperty(value = "本品或赠品 0本品 1赠品")
    @JsonProperty("product_gift")
    private Integer productGift;

    @ApiModelProperty(value = "行选中状态 0未选中 1选中")
    @JsonProperty("line_check_status")
    private Integer lineCheckStatus;

    @ApiModelProperty(value = "交易倍数")
    @JsonProperty("zero_removal_coefficient")
    private Integer zeroRemovalCoefficient;

    @ApiModelProperty(value = "最大订购数量")
    @JsonProperty("max_order_num")
    private Integer maxOrderNum;

    @ApiModelProperty(value = "规格")
    @JsonProperty("spec")
    private String spec;

    @ApiModelProperty(value = "商品属性名称")
    @JsonProperty("product_property_name")
    private String productPropertyName;

    @ApiModelProperty(value = "商品属性编码")
    @JsonProperty("product_property_code")
    private String productPropertyCode;

    @ApiModelProperty(value = "商品品牌名称")
    @JsonProperty("product_brand_name")
    private String productBrandName;

    @ApiModelProperty(value = "商品品牌编码")
    @JsonProperty("product_brand_code")
    private String productBrandCode;

    @ApiModelProperty(value = "商品品类名称")
    @JsonProperty("product_category_name")
    private String productCategoryName;

    @ApiModelProperty(value = "商品品类编码")
    @JsonProperty("product_category_code")
    private String productCategoryCode;

    @ApiModelProperty("批次号")
    @JsonProperty(value = "batch_code")
    private String batchCode;


    @ApiModelProperty("批次日期")
    @JsonProperty(value = "batch_date")
    private Date batchDate;

    @ApiModelProperty("批次编号")
    @JsonProperty(value = "batch_info_code")
    private String batchInfoCode;

    @ApiModelProperty(value = "传入库房编码:1:销售库，2:特卖库")
    @JsonProperty("warehouse_type_code")
    private String warehouseTypeCode;

    /***********************************非数据库字段***********************************/

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

    @ApiModelProperty(value = "库存数量")
    @JsonProperty("stock_num")
    private Integer stockNum;

    @ApiModelProperty("是否能使用A品卷 0否 1是")
    @JsonProperty("coupon_rule")
    private Integer couponRule=0;

    @ApiModelProperty(value = "赠品赠送方式 1、赠完为止")
    @JsonProperty("gift_give_type")
    private Integer giftGiveType;

    @ApiModelProperty(value = "商品行（仅本品）状态  1、正常  2、库存不足，不允许选中和编辑 3、商品禁用或者没查询到商品，不允许选中和编辑")
    @JsonProperty("cart_line_status")
    private Integer cartLineStatus = ErpCartLineStatusEnum.NORMAL.getCode();

    @ApiModelProperty(value = "标签列表")
    @JsonProperty("tag_info_list")
    private List<TagInfo> tagInfoList;

    @ApiModelProperty(value = "本商品待选择活动列表")
    @JsonProperty("activity_list")
    private List<Activity> activityList;

    @ApiModelProperty("是否可售：0为不可售，1为可售")
    @JsonProperty("is_sale")
    private Byte isSale;

    /**活动类型1.满减2.满赠3.折扣4.返点5.特价6.整单*/
    @ApiModelProperty(value = "活动类型1.满减2.满赠3.折扣4.返点5.特价6.整单")
    @JsonProperty("activity_type")
    private Integer activityType;



}
