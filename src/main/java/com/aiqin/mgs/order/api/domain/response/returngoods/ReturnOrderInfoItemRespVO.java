package com.aiqin.mgs.order.api.domain.response.returngoods;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("商品信息")
public class ReturnOrderInfoItemRespVO {
    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("sku编号")
    private String skuCode;

    @ApiModelProperty("sku名称")
    private String skuName;

    @ApiModelProperty("图片地址")
    private String pictureUrl;

    @ApiModelProperty("规格")
    private String spec;

    @ApiModelProperty("颜色名称")
    private String colorName;

    @ApiModelProperty("型号")
    private String model;

    @ApiModelProperty("基商品含量")
    private Integer baseProductContent;

    @ApiModelProperty("商品单位")
    private String unitName;

    @ApiModelProperty("拆零系数")
    private Integer zeroDisassemblyCoefficient;

    @ApiModelProperty("是否是赠品(0否1是)")
    private Integer givePromotion;

    @ApiModelProperty("批次号")
    private String batchNumber;

    @ApiModelProperty("单价")
    private Long price;

    @ApiModelProperty("数量")
    private Long num;

    @ApiModelProperty("总价")
    private Long amount;

    @ApiModelProperty("活动编码(多个，隔开）")
    private String activityCode;

    @ApiModelProperty("商品行号")
    private Long productLineNum;

    @ApiModelProperty("赠品行号")
    private Long promotionLineNum;

    @ApiModelProperty("实际入库数")
    private Long actualInboundNum;

    @ApiModelProperty("商品状态1新品2残品")
    private Integer productStatus;

    @ApiModelProperty("渠道单价")
    private Long channelUnitPrice;

    @ApiModelProperty("渠道总价")
    private Long totalChannelPrice;

    @ApiModelProperty("实际渠道单价")
    private Long actualChannelUnitPrice;

    @ApiModelProperty("实际渠道总价")
    private Long actualTotalChannelPrice;

    @ApiModelProperty("实际单价")
    private Long actualAmount;

    @ApiModelProperty("实际总价")
    private Long actualPrice;
}
