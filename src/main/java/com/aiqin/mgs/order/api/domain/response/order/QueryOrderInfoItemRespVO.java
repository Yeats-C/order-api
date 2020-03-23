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

    @ApiModelProperty("商品类型  0商品 1赠品")
    private Integer productType;

    @ApiModelProperty("分销单价")
    private BigDecimal productAmount;

    @ApiModelProperty("渠道单价")
    private BigDecimal purchaseAmount;

    @ApiModelProperty("数量")
    private Long productCount;

    @ApiModelProperty("分销总价")
    private BigDecimal totalProductAmount;

    @ApiModelProperty("实发商品数量")
    private Long actualProductCount;

    @ApiModelProperty("活动分摊")
    private BigDecimal totalAcivityAmount;

    @ApiModelProperty("优惠分摊")
    private BigDecimal totalPreferentialAmount;

    @ApiModelProperty("活动编码(多个，隔开）")
    private String activityCode;

    @ApiModelProperty("赠品行号")
    private Long giftLineCode;

    @ApiModelProperty("退货数量")
    private Long returnProductCount;

}
