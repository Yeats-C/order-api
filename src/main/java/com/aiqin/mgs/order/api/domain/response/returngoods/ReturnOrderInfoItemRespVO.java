package com.aiqin.mgs.order.api.domain.response.returngoods;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel("商品信息")
public class ReturnOrderInfoItemRespVO {

    @ApiModelProperty("sku编号")
    @JsonProperty("sku_code")
    private String skuCode;

    @ApiModelProperty("sku名称")
    @JsonProperty("sku_name")
    private String skuName;

    @ApiModelProperty("图片地址")
    @JsonProperty("picture_url")
    private String pictureUrl;

    @ApiModelProperty("规格")
    @JsonProperty("product_spec")
    private String productSpec;

    @ApiModelProperty("颜色名称")
    @JsonProperty("color_name")
    private String colorName;

    @ApiModelProperty("型号")
    @JsonProperty("model_code")
    private String modelCode;

    @ApiModelProperty("基商品含量")
    @JsonProperty("base_product_spec")
    private Integer baseProductSpec;

    @ApiModelProperty("商品单位")
    @JsonProperty("unit_name")
    private String unitName;

    @ApiModelProperty("商品类型 0商品 1赠品")
    @JsonProperty("product_type")
    private Integer productType;

    @ApiModelProperty("渠道单价")
    @JsonProperty("channel_amount")
    private BigDecimal channelAmount;

    @ApiModelProperty("分销单价")
    @JsonProperty("product_amount")
    private BigDecimal productAmount;

    @ApiModelProperty("退货数量")
    @JsonProperty("return_product_count")
    private Long returnProductCount;

    @ApiModelProperty("渠道总价")
    @JsonProperty("channel_total_amount")
    private BigDecimal channelTotalAmount;

    @ApiModelProperty("分销总价")
    @JsonProperty("total_product_amount")
    private BigDecimal totalProductAmount;

    @ApiModelProperty("实退数量")
    @JsonProperty("actual_return_product_count")
    private Long actualReturnProductCount;

    @ApiModelProperty("实退渠道总价")
    @JsonProperty("actual_channel_total_amount")
    private BigDecimal actualChannelTotalAmount;

    @ApiModelProperty("实退分销总价")
    @JsonProperty("actual_total_product_amount")
    private BigDecimal actualTotalProductAmount;

    @ApiModelProperty("活动编码(多个，隔开）")
    @JsonProperty("activity_code")
    private String activityCode;

    @ApiModelProperty("赠品行号")
    @JsonProperty("gift_line_code")
    private Long giftLineLode;

    @ApiModelProperty("商品状态0新品1残品")
    @JsonProperty("product_status")
    private Integer productStatus;

}
