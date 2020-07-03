package com.aiqin.mgs.order.api.domain.response.cart;

import com.aiqin.mgs.order.api.domain.TagInfo;
import com.aiqin.mgs.order.api.domain.request.product.BatchRespVo;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@ApiModel("查询供应链商品详情返回值")
public class ErpSkuDetail {

    @ApiModelProperty(value = "sku码")
    @JsonProperty("sku_code")
    private String skuCode;

    @ApiModelProperty(value = "sku名称")
    @JsonProperty("sku_name")
    private String skuName;

    @ApiModelProperty(value = "spu码")
    @JsonProperty("spu_code")
    private String spuCode;

    @ApiModelProperty(value = "spu名称")
    @JsonProperty("spu_name")
    private String spuName;

    @ApiModelProperty(value = "最大订货数")
    @JsonProperty("max_order_num")
    private Integer maxOrderNum;

    @ApiModelProperty(value = "交易倍数")
    @JsonProperty("zero_removalCoefficient")
    private Integer zeroRemovalCoefficient;

    @ApiModelProperty(value = "库存数量")
    @JsonProperty("stock_num")
    private Integer stockNum;

    @ApiModelProperty(value = "生产日期")
    @JsonProperty("production_date")
    private Date productionDate;

    @ApiModelProperty(value = "商品属性名称")
    @JsonProperty("product_property_name")
    private String productPropertyName;

    @ApiModelProperty(value = "商品属性编码")
    @JsonProperty("product_property_code")
    private String productPropertyCode;

    @ApiModelProperty(value = "商品品牌名称")
    @JsonProperty("product_brand_name")
    private String productBrandName;

    @ApiModelProperty(value = "商品品牌编码")
    @JsonProperty("product_brand_code")
    private String productBrandCode;

    @ApiModelProperty(value = "商品品类名称")
    @JsonProperty("product_category_name")
    private String productCategoryName;

    @ApiModelProperty(value = "商品品类编码")
    @JsonProperty("product_category_code")
    private String productCategoryCode;

    @ApiModelProperty(value = "主图片路径")
    @JsonProperty("product_picture_path")
    private String productPicturePath;

    @ApiModelProperty(value = "爱亲分销价")
    @JsonProperty("price_tax")
    private BigDecimal priceTax;

    @ApiModelProperty(value = "颜色名称")
    @JsonProperty("color_name")
    private String colorName;

    @ApiModelProperty(value = "型号")
    @JsonProperty("model_number")
    private String modelNumber;

    @ApiModelProperty(value = "规格")
    @JsonProperty("spec")
    private String spec;

    @ApiModelProperty(value = "标签列表")
    @JsonProperty("tag_info_list")
    private List<TagInfo> tagInfoList;

    @ApiModelProperty("是否可售：0为不可售，1为可售")
    @JsonProperty("is_sale")
    private Byte isSale;

    @ApiModelProperty("厂家指导价")
    @JsonProperty("manufacturer_guide_price")
    private BigDecimal manufacturerGuidePrice;

    @ApiModelProperty("批次号")
    @JsonProperty(value = "batch_code")
    private String batchCode;


    @ApiModelProperty("批次日期")
    @JsonProperty(value = "batch_date")
    private Date batchDate;

    @ApiModelProperty("批次价格")
    @JsonProperty("batch_price")
    private BigDecimal batchPrice=new BigDecimal("0");

    @ApiModelProperty("批次编号")
    @JsonProperty(value = "batch_info_code")
    private String batchInfoCode;

    @ApiModelProperty("批次信息")
    @JsonProperty(value = "batch_list")
    private List<BatchRespVo> batchList;


}
