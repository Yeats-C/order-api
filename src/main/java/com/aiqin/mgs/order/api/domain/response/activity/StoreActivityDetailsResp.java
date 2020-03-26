package com.aiqin.mgs.order.api.domain.response.activity;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author ch
 * @Description
 * @Date 2020/3/10 11:47
 */

@Data
@ApiModel("营销活动商品详情Resp")
public class StoreActivityDetailsResp {

    @ApiModelProperty(value = "营销活动主表（基本信息）")
    @JsonProperty("activity")
    private ActivityDTO activity;

    @ApiModelProperty(value = "营销活动限时折扣表")
    @JsonProperty("activity_product_detail_lists")
    private List<ActivityProductDetailDTO> activityProductDetailLists;

    @ApiModelProperty(value = "营销活动满减折扣表")
    @JsonProperty("activity_full_reductions")
    private List<ActivityProductFullReductionDiscountDTO> activityFullReductions;

    @ApiModelProperty(value = "营销活动满减折扣商品表")
    @JsonProperty("activity_full_reduction_products")
    private List<ActivityProductFullReductionDTO> activityFullReductionProducts;

    @ApiModelProperty(value = "营销活动满赠折扣表")
    @JsonProperty("activity_full_gifts")
    private List<ActivityProductFullGiftDiscountDTO> activityFullGifts;

    @ApiModelProperty(value = "营销活动满赠折扣商品表")
    @JsonProperty("activity_full_gift_porducts")
    private List<ActivityProductFullGiftDTO> activityFullGiftPorducts;

    @ApiModelProperty(value = "营销活动套餐表")
    @JsonProperty("activity_packages")
    private List<ActivityPackageDTO> activityPackages;

    @ApiModelProperty(value = "第n件特价信息")
    @JsonProperty("special_offers")
    private List<ActivityProductSpecialOfferDTO> specialOffers;


}
