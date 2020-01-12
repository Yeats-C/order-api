package com.aiqin.mgs.order.api.domain;


import com.fasterxml.jackson.annotation.JsonProperty;
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
    @ApiModelProperty(value = "id")
    @JsonProperty("id")
    private String id;
    @ApiModelProperty(value = "业务id")
    @JsonProperty("reject_record_detail_id")
    private String rejectRecordDetailId;
    @ApiModelProperty(value = "退供单号")
    @JsonProperty("reject_record_code")
    private String rejectRecordCode;
    @ApiModelProperty(value = "sku编号")
    @JsonProperty("sku_code")
    private String skuCode;
    @ApiModelProperty(value = "sku名称")
    @JsonProperty("sku_name")
    private String skuName;
    @ApiModelProperty(value = "品牌编码")
    @JsonProperty("brand_code")
    private String brandCode;
    @ApiModelProperty(value = "品牌名称")
    @JsonProperty("brand_name")
    private String brandName;
    @ApiModelProperty(value = "品类编码")
    @JsonProperty("category_code")
    private String categoryCode;
    @ApiModelProperty(value = "品类名称")
    @JsonProperty("category_name")
    private String categoryName;
    @ApiModelProperty(value = "规格")
    @JsonProperty("product_spec")
    private String productSpec;
    @ApiModelProperty(value = "颜色编码")
    @JsonProperty("color_code")
    private String colorCode;
    @ApiModelProperty(value = "颜色名称")
    @JsonProperty("color_name")
    private String colorName;
    @ApiModelProperty(value = "型号")
    @JsonProperty("model_code")
    private String modelCode;
    @ApiModelProperty(value = "商品类型   0商品 1赠品 2实物返回")
    @JsonProperty("product_type")
    private Integer productType;
    @ApiModelProperty(value = "商品数量")
    @JsonProperty("product_count")
    private Long productCount;
    @ApiModelProperty(value = "单位编码")
    @JsonProperty("unit_code")
    private String unitCode;
    @ApiModelProperty(value = "单位名称")
    @JsonProperty("unit_name")
    private String unitName;
    @ApiModelProperty(value = "税率")
    @JsonProperty("tax_rate")
    private BigDecimal taxRate;
    @ApiModelProperty(value = "行号")
    @JsonProperty("line_code")
    private Long lineCode;
    @ApiModelProperty(value = "厂商SKU编码")
    @JsonProperty("factory_sku_code")
    private String factorySkuCode;
    @ApiModelProperty(value = "商品含税单价")
    @JsonProperty("tax_amount")
    private BigDecimal taxAmount;
    @ApiModelProperty(value = "商品含税总价")
    @JsonProperty("total_tax_amount")
    private BigDecimal totalTaxAmount;
    @ApiModelProperty(value = "最小单位数量")
    @JsonProperty("total_count")
    private Long totalCount;
    @ApiModelProperty(value = "实际最小单数数量")
    @JsonProperty("actual_total_count")
    private Long actualTotalCount;
    @ApiModelProperty(value = "实际含税总价")
    @JsonProperty("actual_total_tax_amount")
    private BigDecimal actualTotalTaxAmount;
    @ApiModelProperty(value = "0. 启用   1.禁用")
    @JsonProperty("use_status")
    private Integer useStatus;
    @ApiModelProperty(value = "创建人编码")
    @JsonProperty("create_by_id")
    private String createById;
    @ApiModelProperty(value = "创建人名称")
    @JsonProperty("create_by_name")
    private String createByName;
    @ApiModelProperty(value = "修改人编码")
    @JsonProperty("update_by_id")
    private String updateById;
    @ApiModelProperty(value = "修改人名称")
    @JsonProperty("update_by_name")
    private String updateByName;
    @ApiModelProperty(value = "创建时间",example = "2001-01-01 01:01:01")
    @JsonProperty("create_time")
    private Date createTime;
    @ApiModelProperty(value = "修改时间",example = "2001-01-01 01:01:01")
    @JsonProperty("update_time")
    private Date updateTime;
}