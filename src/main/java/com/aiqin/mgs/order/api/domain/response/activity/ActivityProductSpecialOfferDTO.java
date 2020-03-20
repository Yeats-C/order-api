package com.aiqin.mgs.order.api.domain.response.activity;

import com.aiqin.mgs.order.api.base.PagesRequest;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author by wpp25
 * @Classname MarketDTO
 * @Description
 * @Date 2020/3/3 10:47
 */

@Data
@ApiModel("营销活动第n件特价dto")
public class ActivityProductSpecialOfferDTO extends PagesRequest {
    @ApiModelProperty(value = "自增主键")
    @JsonProperty("id")
    private Long id;

    @ApiModelProperty(value = "商品id")
    @JsonProperty("product_id")
    private String productId;

    @ApiModelProperty(value = "营销活动id")
    @JsonProperty("activity_id")
    private String activityId;

    @ApiModelProperty(value = "营销活动编号 也是 编码")
    @JsonProperty("activity_num")
    private String activityNum;

    @ApiModelProperty(value = "skuCode")
    @JsonProperty("sku_code")
    private String skuCode;

    @ApiModelProperty(value = "skuName")
    @JsonProperty("sku_name")
    private String skuName;

    @ApiModelProperty(value = "特价类型 1 ：第二价半价  2： 自定义优惠")
    @JsonProperty("type")
    private Integer type;

    @ApiModelProperty(value = "折扣条件  第多少件")
    @JsonProperty("discount_condition")
    private Integer discountCondition;

    @ApiModelProperty(value = "1 ： 打折  2： 减价")
    @JsonProperty("discount_type")
    private Integer discountType;

    @ApiModelProperty(value = "打（） 折")
    @JsonProperty("reduce_discount")
    private Long reduceDiscount;

    @ApiModelProperty(value = "减（） 元")
    @JsonProperty("reduce_amount")
    private Long reduceAmount;

    @ApiModelProperty(value = "活动创建时间")
    @JsonProperty("create_time")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty(value = "活动修改时间")
    @JsonProperty("update_time")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date updateTime;


    @ApiModelProperty(value = "条形码")
    @JsonProperty("bar_code")
    private String barCode;

    @ApiModelProperty("颜色code")
    @JsonProperty("color_code")
    private String colorCode;

    @ApiModelProperty("颜色")
    @JsonProperty("color_name")
    private String colorName;

    @ApiModelProperty("型号")
    @JsonProperty("model_number")
    private String modelNumber;

    @ApiModelProperty(value = "规格")
    @JsonProperty("spec")
    private String spec;

    @ApiModelProperty(value = "列表图")
    @JsonProperty("logo")
    private String logo;

    @ApiModelProperty(value = "零售价")
    @JsonProperty("price")
    private String price;


    @ApiModelProperty(value = "满足条件后 第n件商品的活动价格")
    @JsonProperty("activity_price")
    private Long activityPrice;

}
