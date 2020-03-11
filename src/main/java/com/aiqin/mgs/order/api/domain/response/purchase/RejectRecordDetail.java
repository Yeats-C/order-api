package com.aiqin.mgs.order.api.domain.response.purchase;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@ApiModel
@Data
public class RejectRecordDetail {
    @ApiModelProperty(value = "")
    @JsonProperty("id")
    private Long id;

    @ApiModelProperty(value = "")
    @JsonProperty("reject_record_id")
    private String rejectRecordId;

    @ApiModelProperty(value = "")
    @JsonProperty("reject_record_detail_id")
    private String rejectRecordDetailId;

    @ApiModelProperty(value = "")
    @JsonProperty("reject_record_code")
    private String rejectRecordCode;

    @ApiModelProperty(value = "条形码")
    @JsonProperty("barcode")
    private String barcode;

    @ApiModelProperty(value = "")
    @JsonProperty("sku_code")
    private String skuCode;

    @ApiModelProperty(value = "")
    @JsonProperty("sku_name")
    private String skuName;

    @ApiModelProperty(value = "")
    @JsonProperty("product_id")
    private String productId;

    @ApiModelProperty(value = "品类id")
    @JsonProperty("category_id")
    private String categoryId;

    @ApiModelProperty(value = "")
    @JsonProperty("category_name")
    private String categoryName;

    @ApiModelProperty(value = "品牌id")
    @JsonProperty("brand_id")
    private String brandId;

    @ApiModelProperty(value = "")
    @JsonProperty("brand_name")
    private String brandName;

    @ApiModelProperty(value = "商品类型 0商品 1赠品 2实物返")
    @JsonProperty("product_type")
    private Integer productType;

    @ApiModelProperty(value = "规格")
    @JsonProperty("product_spec")
    private String productSpec;

    @ApiModelProperty(value = "颜色编码")
    @JsonProperty("color_code")
    private String colorCode;

    @ApiModelProperty(value = "颜色")
    @JsonProperty("color_name")
    private String colorName;

    @ApiModelProperty(value = "厂商sku")
    @JsonProperty("factory_sku_code")
    private String factorySkuCode;

    @ApiModelProperty(value = "型号")
    @JsonProperty("model_number")
    private String modelNumber;

    @ApiModelProperty(value = "单品数量")
    @JsonProperty("single_count")
    private Integer singleCount;

    @ApiModelProperty(value = "单位")
    @JsonProperty("unit_code")
    private String unitCode;

    @ApiModelProperty(value = "单位")
    @JsonProperty("unit_name")
    private String unitName;

    @ApiModelProperty(value = "税率")
    @JsonProperty("tax_rate")
    private BigDecimal taxRate;

    @ApiModelProperty(value = "商品数量")
    @JsonProperty("product_count")
    private Long productCount;

    @ApiModelProperty(value = "含税单价")
    @JsonProperty("product_amount")
    private BigDecimal productAmount;

    @ApiModelProperty(value = "含税总价")
    @JsonProperty("product_total_amount")
    private BigDecimal productTotalAmount;

    @ApiModelProperty(value = "wms 传回来的实际数量")
    @JsonProperty("actual_count")
    private Integer actualCount;

    @ApiModelProperty(value = "wms 传回来的实际金额")
    @JsonProperty("actual_amount")
    private BigDecimal actualAmount;

    @ApiModelProperty(value = "商品批次号")
    @JsonProperty("batch_no")
    private String batchNo;

    @ApiModelProperty(value = "批次备注")
    @JsonProperty("batch_remark")
    private String batchRemark;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "批次生产日期")
    @JsonProperty("batch_create_time")
    private Date batchCreateTime;

    @ApiModelProperty(value = "")
    @JsonProperty("create_by_id")
    private String createById;

    @ApiModelProperty(value = "")
    @JsonProperty("create_by_name")
    private String createByName;

    @ApiModelProperty(value = "")
    @JsonProperty("update_by_id")
    private String updateById;

    @ApiModelProperty(value = "")
    @JsonProperty("update_by_name")
    private String updateByName;

    @ApiModelProperty(value = "")
    @JsonProperty("create_time")
    private Date createTime;

    @ApiModelProperty(value = "")
    @JsonProperty("update_time")
    private Date updateTime;

    @ApiModelProperty(value="行号")
    private Integer linnum;
}