package com.aiqin.mgs.order.api.domain.request.gift;

import com.aiqin.mgs.order.api.domain.request.cart.ErpCartAddSkuItem;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("购物车批量修改请求参数")
public class GiftCartUpdateRequest {

    @ApiModelProperty(value = "购物车唯一标识 修改必填")
    @JsonProperty("cart_id")
    private String cartId;

    @ApiModelProperty(value = "数量 修改必填 不修改传原来的值")
    @JsonProperty("amount")
    private Integer amount;

    @ApiModelProperty(value = "活动id  修改必填 不修改传原来的值")
    @JsonProperty("activity_id")
    private String activityId;

    @ApiModelProperty(value = "选中状态： 修改必填  0、不选 1、选中 必填，不修改传原来的值")
    @JsonProperty("line_check_status")
    private Integer lineCheckStatus;


    @ApiModelProperty(value = "门店Id【新增必填】")
    @JsonProperty("store_id")
    private String storeId;

    @ApiModelProperty(value = "订单类型 1直送 2配送【新增必填】")
    @JsonProperty("product_type")
    private Integer productType;

    @ApiModelProperty(value = "sku列表【新增必填】")
    @JsonProperty("products")
    private List<ErpCartAddSkuItem> products;

}
