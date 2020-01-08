package com.aiqin.mgs.order.api.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@ApiModel("退货单表")
public class ReturnOrderDetail {

    @ApiModelProperty(value = "主键id")
    @JsonProperty("id")
    private Long id;

    @ApiModelProperty(value = "业务id")
    @JsonProperty("return_order_detail_id")
    private String returnOrderDetailId;

    @ApiModelProperty(value = "退货单号")
    @JsonProperty("return_order_code")
    private String returnOrderCode;

    @ApiModelProperty(value = "sku编号")
    @JsonProperty("sku_code")
    private String skuCode;

    @ApiModelProperty(value = "sku名称")
    @JsonProperty("sku_name")
    private String skuName;

    @ApiModelProperty(value = "图片地址")
    @JsonProperty("picture_url")
    private String pictureUrl;

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

    @ApiModelProperty(value = "base_product_spec")
    @JsonProperty("base_product_spec")
    private Long baseProductSpec;

    @ApiModelProperty(value = "单位编码")
    @JsonProperty("unit_code")
    private String unitCode;

    @ApiModelProperty(value = "单位名称")
    @JsonProperty("unit_name")
    private String unitName;

    @ApiModelProperty(value = "商品类型 0商品 1赠品")
    @JsonProperty("product_type")
    private Integer productType;

    @ApiModelProperty(value = "拆零系数")
    @JsonProperty("zero_disassembly_coefficient")
    private Long zeroDisassemblyCoefficient;

    @ApiModelProperty(value = "活动编码(多个，隔开）")
    @JsonProperty("activity_code")
    private String activityCode;

    @ApiModelProperty(value = "商品单价")
    @JsonProperty("product_amount")
    private BigDecimal productAmount;

    @ApiModelProperty(value = "商品总价")
    @JsonProperty("total_product_amount")
    private BigDecimal totalProductAmount;

    @ApiModelProperty(value = "退货数量")
    @JsonProperty("return_product_count")
    private Long returnProductCount;

    @ApiModelProperty(value = "实退数量")
    @JsonProperty("actual_return_product_count")
    private Long actualReturnProductCount;

    @ApiModelProperty(value = "实退商品总价")
    @JsonProperty("actual_total_product_amount")
    private BigDecimal actualTotalProductAmount;

    @ApiModelProperty(value = "商品状态0新品1残品")
    @JsonProperty("product_status")
    private Integer productStatus;

    @ApiModelProperty(value = "税率")
    @JsonProperty("tax_rate")
    private BigDecimal taxRate;

    @ApiModelProperty(value = "行号")
    @JsonProperty("line_code")
    private Long lineCode;

    @ApiModelProperty(value = "0. 启用   1.禁用")
    @JsonProperty("use_status")
    private Integer useStatus;

    @ApiModelProperty(value = "来源单号")
    @JsonProperty("source_code")
    private String sourceCode;

    @ApiModelProperty(value = "来源类型")
    @JsonProperty("source_type")
    private Integer sourceType;

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

    @ApiModelProperty(value = "创建时间")
    @JsonProperty("create_time")
    private Date createTime;

    @ApiModelProperty(value = "修改时间")
    @JsonProperty("update_time")
    private Date updateTime;

    @ApiModelProperty(value = "问题描述")
    @JsonProperty("remark")
    private String remark;

    @ApiModelProperty(value = "多个凭证以逗号隔开")
    @JsonProperty("evidence_url")
    private String evidenceUrl;

    @ApiModelProperty(value = "均摊后单价")
    @JsonProperty("preferential_amount")
    private BigDecimal preferentialAmount;
}