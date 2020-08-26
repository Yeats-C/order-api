package com.aiqin.mgs.order.api.domain.response.order;

import com.aiqin.mgs.order.api.domain.request.product.BatchRespVo;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author knight.xie
 * @version 1.0
 * @className ProductSkuRespVo
 * @date 2019/5/14 15:05
 * @description TODO
 */
@ApiModel("SKU基本信息返回")
@Data
public class ProductSkuRespVo2 {

    @ApiModelProperty(value = "主键ID")
    @JsonProperty("id")
    private Long id;

    @ApiModelProperty("所属商品编码")
    @JsonProperty("spu_code")
    private String spuCode;

    @ApiModelProperty("商品名称")
    @JsonProperty("spu_name")
    private String spuName;

    @ApiModelProperty(value = "sku编号")
    @JsonProperty("sku_code")
    private String skuCode;

    @ApiModelProperty("sku名称")
    @JsonProperty("sku_name")
    private String skuName;

    @ApiModelProperty("商品品牌code")
    @JsonProperty("product_brand_code")
    private String productBrandCode;

    @ApiModelProperty("商品品牌")
    @JsonProperty("product_brand_name")
    private String productBrandName;

    @ApiModelProperty("商品品类code")
    @JsonProperty("product_category_code")
    private String productCategoryCode;

    @ApiModelProperty("商品品类名称")
    @JsonProperty("product_category_name")
    private String productCategoryName;

    @ApiModelProperty("商品/赠品(0:商品，1:赠品)")
    @JsonProperty("goods_gifts")
    private Byte goodsGifts;

    @ApiModelProperty("商品属性code")
    @JsonProperty("product_property_code")
    private String productPropertyCode;

    @ApiModelProperty("商品属性名称")
    @JsonProperty("product_property_name")
    private String productPropertyName;

    @ApiModelProperty("采购组名称")
    @JsonProperty("procurement_section_name")
    private String procurementSectionName;

    @ApiModelProperty("颜色code")
    @JsonProperty("color_code")
    private String colorCode;

    @ApiModelProperty("颜色名称")
    @JsonProperty("color_name")
    private String colorName;

    @ApiModelProperty("型号")
    @JsonProperty("model_number")
    private String modelNumber;


    @ApiModelProperty("厂家指导价")
    @JsonProperty("manufacturer_guide_price")
    private BigDecimal manufacturerGuidePrice;


    @ApiModelProperty("最大订购数-销售信息独有")
    @JsonProperty("max_order_num")
    private Integer maxOrderNum;

    @ApiModelProperty("交易倍数")
    @JsonProperty("zero_removalCoefficient")
    private Long zeroRemovalCoefficient;

    @ApiModelProperty(value = "公司编码",hidden = true)
    @JsonProperty("company_code")
    private String companyCode;

    @ApiModelProperty(value = "公司名称",hidden = true)
    @JsonProperty("company_name")
    private String companyName;


    @ApiModelProperty("主图片路径")
    @JsonProperty("product_picture_path")
    private String productPicturePath;

    @ApiModelProperty("规格")
    @JsonProperty("spec")
    private String spec;


//    @ApiModelProperty("爱亲分销价")
//    private BigDecimal priceTax;
//
//    @ApiModelProperty("爱亲采购价")
//    private BigDecimal priceTax1;

    @ApiModelProperty("爱亲分销价")
    @JsonProperty("price_tax")
    private BigDecimal priceTax=new BigDecimal("0");

    @ApiModelProperty("爱亲采购价")
    @JsonProperty("price_tax1")
    private BigDecimal priceTax1 =new BigDecimal("0");

    @ApiModelProperty("爱亲销售价")
    @JsonProperty("price_tax2")
    private BigDecimal priceTax2=new BigDecimal("0");

/*    @ApiModelProperty("会员价")
    @JsonProperty("price_tax3")
    private BigDecimal priceTax3=new BigDecimal("0");*/

    @ApiModelProperty("会员价")
    @JsonProperty("price_tax3")
    private BigDecimal priceTax3;

    @ApiModelProperty("销项税率")
    @JsonProperty("output_tax_rate")
    private Long outputTaxRate;



    @ApiModelProperty(value = "sku库存数量")
    @JsonProperty("stock_num")
    private Long  stockNum;

    @ApiModelProperty("是否可售：0为不可售，1为可售")
    @JsonProperty("is_sale")
    private Byte isSale;

    @ApiModelProperty("批次对应库房类型")
    @JsonProperty(value = "warehouse_type")
    private Integer warehouseType;

    @ApiModelProperty("批次信息")
    @JsonProperty(value = "batch_list")
    private List<BatchRespVo> batchList;

    @ApiModelProperty("仓编码(物流中心编码)")
    @JsonProperty(value = "transport_center_code")
    private String transportCenterCode;

    @ApiModelProperty("仓名称(物流中心名称)")
    @JsonProperty(value = "transport_center_name")
    private String transportCenterName;

    @ApiModelProperty("库房编码")
    @JsonProperty(value = "warehouse_code")
    private String warehouseCode;

    @ApiModelProperty("库房名称")
    @JsonProperty(value = "warehouse_name")
    private String warehouseName;

}
