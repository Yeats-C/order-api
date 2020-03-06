package com.aiqin.mgs.order.api.domain.response.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;


@Data
@ApiModel("订单详情商品列表信息")
public class QueryOrderInfoItemRespVO {

    @ApiModelProperty("主键")
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

    @ApiModelProperty("商品单位")
    private String unitName;

    @ApiModelProperty("拆零系数")
    private Integer zeroDisassemblyCoefficient;

    @ApiModelProperty("是否是赠品(0否1是)")
    private Integer givePromotion;

    @ApiModelProperty("商品类型  0商品 1赠品")
    private Integer productType;

    @ApiModelProperty("批次号")
    private String batchNumber;

    @ApiModelProperty("分销单价")
    private BigDecimal price;

    @ApiModelProperty("渠道单价")
    private BigDecimal channelUnitPrice;

    @ApiModelProperty("渠道总价")
    private BigDecimal totalChannelPrice;

    @ApiModelProperty("数量")
    private Long num;

    @ApiModelProperty("分销总价 分销单价*数量")
    private Long amount;

    @ApiModelProperty("活动分摊")
    private BigDecimal activityApportionment;

    @ApiModelProperty("优惠分摊")
    private BigDecimal preferentialAllocation;

    @ApiModelProperty("实发数量")
    private Long actualDeliverNum;

    @ApiModelProperty("活动编码(多个，隔开）")
    private String activityCode;

    @ApiModelProperty("商品行号")
    private Long productLineNum;

    @ApiModelProperty("赠品行号")
    private Long promotionLineNum;

    @ApiModelProperty("退货数量")
    private Long returnNum;

    @ApiModelProperty("实际渠道单价")
    private BigDecimal actualChannelUnitPrice;

    @ApiModelProperty("实际渠道总价")
    private BigDecimal actualTotalChannelPrice;

    @ApiModelProperty("实际分销总价")
    private BigDecimal actualAmount;

    @ApiModelProperty("实际分销单价")
    private BigDecimal actualPrice;

    @ApiModelProperty("熙耘采购价")
    private BigDecimal purchaseAmount;
}
