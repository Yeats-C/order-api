package com.aiqin.mgs.order.api.domain.po.order;

import com.aiqin.mgs.order.api.base.PagesRequest;
import com.aiqin.mgs.order.api.component.enums.ErpProductGiftEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
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
@ApiModel("订单商品明细行实体")
@JsonInclude(JsonInclude.Include.ALWAYS)
public class ErpOrderItem extends PagesRequest {

    @ApiModelProperty(value = "主键")
    @JsonProperty("id")
    private Long id;

    @ApiModelProperty(value = "订单行id")
    @JsonProperty("orderInfo_detail_id")
    private String orderInfoDetailId;

    @ApiModelProperty(value = "订单id")
    @JsonProperty("order_store_id")
    private String orderStoreId;

    @ApiModelProperty(value = "订单号")
    @JsonProperty("order_store_code")
    private String orderStoreCode;

    @ApiModelProperty(value = "行号")
    @JsonProperty("line_code")
    private Long lineCode;

    @ApiModelProperty(value = "spu编码")
    @JsonProperty("spu_code")
    private String spuCode;

    @ApiModelProperty(value = "spu名称")
    @JsonProperty("spu_name")
    private String spuName;

    @ApiModelProperty(value = "sku编码")
    @JsonProperty("sku_code")
    private String skuCode;

    @ApiModelProperty(value = "sku名称")
    @JsonProperty("sku_name")
    private String skuName;

    @ApiModelProperty(value = "条形码")
    @JsonProperty("bar_code")
    private String barCode;

    @ApiModelProperty(value = "图片url")
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

    @ApiModelProperty(value = "单位编码")
    @JsonProperty("unit_code")
    private String unitCode;

    @ApiModelProperty(value = "单位名称")
    @JsonProperty("unit_name")
    private String unitName;

    @ApiModelProperty(value = "折零系数")
    @JsonProperty("zero_disassembly_coefficient")
    private Long zeroDisassemblyCoefficient;

    @ApiModelProperty(value = "商品类型 0商品（本品） 1赠品 2兑换赠品")
    @JsonProperty("product_type")
    private Integer productType;

    @ApiModelProperty(value = "商品类型描述")
    @JsonProperty("product_type_desc")
    private String productTypeDesc;

    @ApiModelProperty(value = "商品属性编码")
    @JsonProperty("product_property_code")
    private String productPropertyCode;

    @ApiModelProperty(value = "商品属性名称")
    @JsonProperty("product_property_name")
    private String productPropertyName;

    @ApiModelProperty(value = "供应商编码")
    @JsonProperty("supplier_code")
    private String supplierCode;

    @ApiModelProperty(value = "供应商名称")
    @JsonProperty("supplier_name")
    private String supplierName;

    @ApiModelProperty(value = "数量")
    @JsonProperty("product_count")
    private Long productCount;

    @ApiModelProperty(value = "单价（元）")
    @JsonProperty("product_amount")
    private BigDecimal productAmount;

    @ApiModelProperty(value = "活动价（元）")
    @JsonProperty("activity_price")
    private BigDecimal activityPrice;

    @ApiModelProperty(value = "含税采购价（元）")
    @JsonProperty("purchase_amount")
    private BigDecimal purchaseAmount;

    @ApiModelProperty(value = "行总价（元）")
    @JsonProperty("total_product_amount")
    private BigDecimal totalProductAmount;

    @ApiModelProperty(value = "实际商品总价（发货商品总价,均摊后金额的比例）（元）")
    @JsonProperty("actual_total_product_amount")
    private BigDecimal actualTotalProductAmount;

    @ApiModelProperty(value = "优惠分摊总金额（分摊后金额）（元）")
    @JsonProperty("total_preferential_amount")
    private BigDecimal totalPreferentialAmount;

    @ApiModelProperty(value = "分摊后单价（元）")
    @JsonProperty("preferential_amount")
    private BigDecimal preferentialAmount;

    //TODO 增加A品券优惠金额
//    @ApiModelProperty(value = "A品券优惠金额（元）")
//    @JsonProperty("apin_coupon_amount")
//    private BigDecimal apinCouponAmount;

    @ApiModelProperty(value = "活动优惠总金额,包括活动优惠和优惠券优惠（元）")
    @JsonProperty("total_acivity_amount")
    private BigDecimal totalAcivityAmount;

    @ApiModelProperty(value = "门店实收数量")
    @JsonProperty("actual_inbound_count")
    private Long actualInboundCount;

    @ApiModelProperty(value = "实发数量")
    @JsonProperty("actual_product_count")
    private Long actualProductCount;

    @ApiModelProperty(value = "退货数量")
    @JsonProperty("return_product_count")
    private Long returnProductCount;

    @ApiModelProperty(value = "税率")
    @JsonProperty("tax_rate")
    private BigDecimal taxRate;

    @ApiModelProperty(value = "活动编码(多个，隔开）")
    @JsonProperty("activity_code")
    private String activityCode;

    @ApiModelProperty(value = "赠品行号")
    @JsonProperty("gift_line_code")
    private Long giftLineCode;

    @ApiModelProperty(value = "公司编码")
    @JsonProperty("company_code")
    private String companyCode;

    @ApiModelProperty(value = "公司名称")
    @JsonProperty("company_name")
    private String companyName;

    @ApiModelProperty(value = "签收数量差异原因")
    @JsonProperty("sign_difference_reason")
    private String signDifferenceReason;

    @ApiModelProperty(value = "单个商品毛重(kg)")
    @JsonProperty("box_gross_weight")
    private BigDecimal boxGrossWeight;

    @ApiModelProperty(value = "单个商品包装体积(mm³)")
    @JsonProperty("box_volume")
    private BigDecimal boxVolume;

    @ApiModelProperty(value = "单品活动id")
    @JsonProperty("activity_id")
    private String activityId;

    @ApiModelProperty(value = "0. 启用   1.禁用")
    @JsonProperty("use_status")
    private String useStatus;

    @ApiModelProperty(value = "创建人编码")
    @JsonProperty("create_by_id")
    private String createById;

    @ApiModelProperty(value = "创建人名称")
    @JsonProperty("create_by_name")
    private String createByName;

    @ApiModelProperty(value = "更新人编码")
    @JsonProperty("update_by_id")
    private String updateById;

    @ApiModelProperty(value = "更新人名称")
    @JsonProperty("update_by_name")
    private String updateByName;

    @ApiModelProperty(value = "创建时间")
    @JsonProperty("create_time")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    @JsonProperty("update_time")
    private Date updateTime;

    @ApiModelProperty(value = "是否活动商品0否1是")
    @JsonProperty("is_activity")
    private Integer isActivity;

    @ApiModelProperty(value = "订单状态")
    @JsonProperty("order_status")
    private Integer orderStatus;

    @ApiModelProperty(value = "门店编码")
    @JsonProperty("store_code")
    private String storeCode;

    @ApiModelProperty(value = "门店名称")
    @JsonProperty("store_name")
    private String storeName;

    @ApiModelProperty(value = "商品品牌编码")
    @JsonProperty("product_brand_code")
    private String productBrandCode;

    @ApiModelProperty(value = "商品品牌名称")
    @JsonProperty("product_brand_name")
    private String productBrandName;

    @ApiModelProperty(value = "商品品类编码")
    @JsonProperty("product_category_code")
    private String productCategoryCode;

    @ApiModelProperty(value = "商品品类名称")
    @JsonProperty("product_category_name")
    private String productCategoryName;

    /***仅活动优惠金额,用于统计*/
    @JsonProperty("activity_discount_amount")
    private BigDecimal activityDiscountAmount;

    /***仅A品优惠金额，用于统计*/
    @ApiModelProperty(value = "本行A品券优惠总额度")
    @JsonProperty("top_coupon_discount_amount")
    private BigDecimal topCouponDiscountAmount;

    @ApiModelProperty(value = "本行A品券优惠单品额度")
    @JsonProperty("top_coupon_amount")
    private BigDecimal topCouponAmount;

    @ApiModelProperty(value = "爱亲分销价【订单详情展示字段】")
    @JsonProperty("price_tax")
    private BigDecimal priceTax;

    @ApiModelProperty(value = "使用赠品额度【订单详情展示字段】")
    @JsonProperty("used_gift_quota")
    private BigDecimal usedGiftQuota;

    /***批次*/
    @ApiModelProperty(value = "批次号")
    @JsonProperty("batch_code")
    private String batchCode;

    @ApiModelProperty(value = "批次号时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JsonProperty("batch_date")
    private String batchDate;

    @ApiModelProperty(value = "批次编码号")
    @JsonProperty("batch_info_code")
    private String batchInfoCode;

//    @ApiModelProperty("批次编号")
//    @JsonProperty(value = "batch_info_code")
//    private String batchInfoCode;

    @ApiModelProperty(value = "传入库房编码:1:销售库，2:特卖库")
    @JsonProperty("warehouse_type_code")
    private String warehouseTypeCode;

    /**活动类型1.满减2.满赠3.折扣4.返点5.特价6.整单7.买赠*/
    @ApiModelProperty(value = "活动类型1.满减2.满赠3.折扣4.返点5.特价6.整单7.买赠")
    @JsonProperty("activity_type")
    private Integer activityType;

    @ApiModelProperty(value = "仓库编码")
    @JsonProperty("transport_center_code")
    private String transportCenterCode;

    @ApiModelProperty(value = "仓库名称")
    @JsonProperty("transport_center_name")
    private String transportCenterName;

    @ApiModelProperty(value = "库房编码")
    @JsonProperty("warehouse_code")
    private String warehouseCode;

    @ApiModelProperty(value = "库房名称")
    @JsonProperty("warehouse_name")
    private String warehouseName;

    @ApiModelProperty("批次类型  0：月份批次  1：非月份批次")
    @JsonProperty(value = "batch_type")
    private Integer batchType;

    @ApiModelProperty(value="销项税率")
    @JsonProperty("output_tax_rate")
    private BigDecimal outputTaxRate;


    public String getProductTypeDesc() {
        return ErpProductGiftEnum.getEnumDesc(productType);
    }
}
