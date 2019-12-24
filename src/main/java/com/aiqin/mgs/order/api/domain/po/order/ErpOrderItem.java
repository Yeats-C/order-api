package com.aiqin.mgs.order.api.domain.po.order;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单商品明细行实体
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/12/9 10:11
 */
@Data
@JsonInclude(JsonInclude.Include.ALWAYS)
public class ErpOrderItem {

    /***主键*/
    private Long id;
    /***业务id*/
    @ApiModelProperty(value = "订单行id")
    private String orderInfoDetailId;
    /***订单id*/
    private String orderStoreId;
    /***订单号*/
    private String orderStoreCode;
    /***spu编码*/
    @ApiModelProperty(value = "spu编码")
    private String spuCode;
    /***spu名称*/
    @ApiModelProperty(value = "spu名称")
    private String spuName;
    /***sku编码*/
    @ApiModelProperty(value = "sku编码")
    private String skuCode;
    /***sku名称*/
    @ApiModelProperty(value = "sku名称")
    private String skuName;
    @ApiModelProperty(value = "条形码")
    private String barCode;
    /***图片地址*/
    @ApiModelProperty(value = "图片url")
    private String pictureUrl;
    /***规格*/
    @ApiModelProperty(value = "规格")
    private String productSpec;
    /***颜色编码*/
    @ApiModelProperty(value = "颜色编码")
    private String colorCode;
    /***颜色名称*/
    @ApiModelProperty(value = "颜色名称")
    private String colorName;
    /***型号*/
    @ApiModelProperty(value = "型号")
    private String modelCode;
    /***单位编码*/
    @ApiModelProperty(value = "单位编码")
    private String unitCode;
    /***单位名称*/
    @ApiModelProperty(value = "单位名称")
    private String unitName;
    /***折零系数*/
    private Long zeroDisassemblyCoefficient;
    /***商品类型  0商品 1赠品*/
    @ApiModelProperty(value = "商品类型 0商品（本品） 1赠品")
    private Integer productType;
    /***商品数量*/
    @ApiModelProperty(value = "数量")
    private Long productCount;
    /***商品单价*/
    @ApiModelProperty(value = "单价")
    private BigDecimal productAmount;
    /***含税采购价*/
    @ApiModelProperty(value = "含税采购价")
    private BigDecimal purchaseAmount;
    /***商品总价*/
    @ApiModelProperty(value = "行总价")
    private BigDecimal totalProductAmount;
    /***实际商品总价（发货商品总价）*/
    @ApiModelProperty(value = "发货商品总价")
    private BigDecimal actualTotalProductAmount;
    /***优惠分摊总金额（分摊后金额）*/
    @ApiModelProperty(value = "行均摊总价")
    private BigDecimal totalPreferentialAmount;
    /***活动优惠总金额*/
    private BigDecimal totalAcivityAmount;
    /***实收数量（门店）*/
    @ApiModelProperty(value = "门店实收数量")
    private Long actualInboundCount;
    /***实发商品数量*/
    @ApiModelProperty(value = "实发数量")
    private Long actualProductCount;
    /***退货数量*/
    private Long returnProductCount;
    /***税率*/
    private BigDecimal taxRate;
    /***活动编码(多个，隔开）*/
    private String activityCode;
    /***行号*/
    @ApiModelProperty(value = "行号")
    private Long lineCode;
    /***赠品行号*/
    private Long giftLineCode;
    /***公司编码*/
    private String companyCode;
    /***公司名称*/
    private String companyName;
    /***签收数量差异原因*/
    private String signDifferenceReason;

    /***0. 启用   1.禁用*/
    private String useStatus;
    /***创建人编码*/
    private String createById;
    /***创建人名称*/
    private String createByName;
    /***更新人编码*/
    private String updateById;
    /***更新人名称*/
    private String updateByName;
    /***创建时间*/
    private Date createTime;
    /***更新时间*/
    private Date updateTime;
}
