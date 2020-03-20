package com.aiqin.mgs.order.api.domain.response.activity;

import com.aiqin.mgs.order.api.domain.response.BaseDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("营销中心门店限时活动商品明细")
@Data
public class ActivityProductDetailDTO extends BaseDTO {

    @ApiModelProperty(value = "活动id，业务id")
    @JsonProperty("activity_id")
    private String activityId;

    @ApiModelProperty(value = "sku_code")
    @JsonProperty("sku_code")
    private String skuCode;

    @ApiModelProperty(value = "商品logo图")
    @JsonProperty("logo")
    private String logo;

    @ApiModelProperty(value = "渠道，0为门店，1为网点，2为门店和网店")
    @JsonProperty("channel")
    private Integer channel;

    @ApiModelProperty(value = "活动名称")
    @JsonProperty("product_name")
    private String productName;

    @ApiModelProperty(value = "活动编码")
    @JsonProperty("product_code")
    private String productCode;

    @ApiModelProperty(value = "原价")
    @JsonProperty("price")
    private Long price;

    @ApiModelProperty(value = "折扣")
    @JsonProperty("discount")
    private Integer discount;

    @ApiModelProperty(value = "减价金额")
    @JsonProperty("reduce")
    private Long reduce;

    @ApiModelProperty(value = "实际价格")
    @JsonProperty("actual_price")
    private Long actualPrice;

    @ApiModelProperty(value = "活动关联商品的状态，0为启用，1为禁用")
    @JsonProperty("status")
    private Integer status;

    @ApiModelProperty(value = "是否删除，0为否，1为是")
    @JsonProperty("is_delete")
    private Integer isDelete;
}
