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
@ApiModel("营销活动套餐对应的商品dto")
public class ActivityPackageProductDTO extends PagesRequest {
    @ApiModelProperty(value = "自增主键")
    @JsonProperty("id")
    private Long id;

    @ApiModelProperty(value = "活动商品id")
    @JsonProperty("product_id")
    private String productId;

    @ApiModelProperty(value = "套餐包id")
    @JsonProperty("package_id")
    private String packageId;

    @ApiModelProperty(value = "活动id")
    @JsonProperty("activity_id")
    private String activityId;

    @ApiModelProperty(value = "活动编号 也是 活动编码")
    @JsonProperty("activity_num")
    private String activityNum;

    @ApiModelProperty(value = "skuCode")
    @JsonProperty("sku_code")
    private String skuCode;

    @ApiModelProperty(value = "skuName")
    @JsonProperty("sku_name")
    private String skuName;

    @ApiModelProperty(value = "sku数量")
    @JsonProperty("sku_count")
    private Integer skuCount;

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
}
