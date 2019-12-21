package com.aiqin.mgs.order.api.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 退供商品信息
 * TableName reject_record_detail
 */
@Data
@ApiModel("退供商品信息")
@ToString(callSuper = true)
public class RejectRecordDetail {
    @ApiModelProperty(value = "业务id")
    private String rejectRecordDetailId;
    @ApiModelProperty(value = "退供单号")
    private String rejectRecordCode;
    @ApiModelProperty(value = "sku编号")
    private String skuCode;
    @ApiModelProperty(value = "sku名称")
    private String skuName;
    @ApiModelProperty(value = "品牌编码")
    private String brandCode;
    @ApiModelProperty(value = "品牌名称")
    private String brandName;
    @ApiModelProperty(value = "品类编码")
    private String categoryCode;
    @ApiModelProperty(value = "品类名称")
    private String categoryName;
    @ApiModelProperty(value = "规格")
    private String productSpec;
    @ApiModelProperty(value = "颜色编码")
    private String colorCode;
    @ApiModelProperty(value = "颜色名称")
    private String colorName;
    @ApiModelProperty(value = "型号")
    private String modelCode;
    @ApiModelProperty(value = "商品类型   0商品 1赠品 2实物返回")
    private Boolean productType;
    @ApiModelProperty(value = "商品数量")
    private Long productCount;
    @ApiModelProperty(value = "单位编码")
    private String unitCode;
    @ApiModelProperty(value = "单位名称")
    private String unitName;
    @ApiModelProperty(value = "税率")
    private BigDecimal taxRate;
    @ApiModelProperty(value = "行号")
    private String lineCode;
    @ApiModelProperty(value = "厂商SKU编码")
    private String factorySkuCode;
    @ApiModelProperty(value = "商品含税单价")
    private BigDecimal taxAmount;
    @ApiModelProperty(value = "商品含税总价")
    private BigDecimal totalTaxAmount;
    @ApiModelProperty(value = "最小单位数量")
    private Long totalCount;
    @ApiModelProperty(value = "实际最小单数数量")
    private Long actualTotalCount;
    @ApiModelProperty(value = "实际含税总价")
    private BigDecimal actualTotalTaxAmount;
    @ApiModelProperty(value = "0. 启用   1.禁用")
    private Boolean useStatus;
    @ApiModelProperty(value = "创建人编码")
    private String createById;
    @ApiModelProperty(value = "创建人名称")
    private String createByName;
    @ApiModelProperty(value = "修改人编码")
    private String updateById;
    @ApiModelProperty(value = "修改人名称")
    private String updateByName;
    @ApiModelProperty(value = "创建时间",example = "2001-01-01 01:01:01")
    private Date createTime;
    @ApiModelProperty(value = "修改时间",example = "2001-01-01 01:01:01")
    private Date updateTime;
}