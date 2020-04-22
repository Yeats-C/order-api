package com.aiqin.mgs.order.api.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@ApiModel("退货单表")
public class ReturnOrderDetail {

    @ApiModelProperty(value = "主键id")
    private Long id;

    @ApiModelProperty(value = "业务id")
    private String returnOrderDetailId;

    @ApiModelProperty(value = "退货单号")
    private String returnOrderCode;

    @ApiModelProperty(value = "sku编号")
    private String skuCode;

    @ApiModelProperty(value = "sku名称")
    private String skuName;

    @ApiModelProperty(value = "图片地址")
    private String pictureUrl;

    @ApiModelProperty(value = "规格")
    private String productSpec;

    @ApiModelProperty(value = "颜色编码")
    private String colorCode;

    @ApiModelProperty(value = "颜色名称")
    private String colorName;

    @ApiModelProperty(value = "型号")
    private String modelCode;

    @ApiModelProperty(value = "base_product_spec")
    private Long baseProductSpec;

    @ApiModelProperty(value = "单位编码")
    private String unitCode;

    @ApiModelProperty(value = "单位名称")
    private String unitName;

    @ApiModelProperty(value = "商品类型 0商品 1赠品 2:兑换赠品")
    private Integer productType;

    @ApiModelProperty(value = "拆零系数")
    private Long zeroDisassemblyCoefficient;

    @ApiModelProperty(value = "活动编码(多个，隔开）")
    private String activityCode;

    @ApiModelProperty(value = "商品单价")
    private BigDecimal productAmount;

    @ApiModelProperty(value = "商品总价")
    private BigDecimal totalProductAmount;

    @ApiModelProperty(value = "退货数量")
    private Long returnProductCount;

    @ApiModelProperty(value = "实退数量")
    private Long actualReturnProductCount;

    @ApiModelProperty(value = "实退商品总价")
    private BigDecimal actualTotalProductAmount;

    @ApiModelProperty(value = "商品状态0新品1残品")
    private Integer productStatus;

    @ApiModelProperty(value = "税率")
    private BigDecimal taxRate;

    @ApiModelProperty(value = "行号")
    private Long lineCode;

    @ApiModelProperty(value = "0. 启用   1.禁用")
    private Integer useStatus;

    @ApiModelProperty(value = "来源单号")
    private String sourceCode;

    @ApiModelProperty(value = "来源类型")
    private Integer sourceType;

    @ApiModelProperty(value = "创建人编码")
    private String createById;

    @ApiModelProperty(value = "创建人名称")
    private String createByName;

    @ApiModelProperty(value = "修改人编码")
    private String updateById;

    @ApiModelProperty(value = "修改人名称")
    private String updateByName;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty(value = "修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    @ApiModelProperty(value = "问题描述")
    private String remark;

    @ApiModelProperty(value = "多个凭证以逗号隔开")
    private String evidenceUrl;

    @ApiModelProperty(value = "均摊后单价")
    private BigDecimal preferentialAmount;

    @ApiModelProperty(value = "商品品牌编码")
    private String productCategoryCode;

    @ApiModelProperty(value = "商品品牌名称")
    private String productCategoryName;

    @ApiModelProperty(value = "条形码")
    private String barCode;

}