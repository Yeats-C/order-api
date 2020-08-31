package com.aiqin.mgs.order.api.domain;

import com.aiqin.mgs.order.api.domain.po.order.ErpBatchInfo;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

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

    @ApiModelProperty(value = "退款金额 ----传")
    private BigDecimal refundAmount;

    @ApiModelProperty(value = "退供金额 ----传")
    private BigDecimal withdrawAmount;

    @NotBlank(message = "仓库编码不能为空 ----传")
    @ApiModelProperty("仓库编码")
    private String warehouseCode;

    @NotBlank(message = "仓库名称不能为空 ----传")
    @ApiModelProperty("仓库名称")
    private String warehouseName;

    @ApiModelProperty("配送中心编码 ---传" )
    private String transportCenterCode;

    @ApiModelProperty("配送中心名称 ---传")
    private String transportCenterName;

    @ApiModelProperty(value = "已退货数量 ------传 ")
    private Long quantityReturnedCount;


    /***仅A品优惠金额，用于统计*/
    @ApiModelProperty(value = "本行A品券优惠总额度")
    private BigDecimal topCouponDiscountAmount;

    @ApiModelProperty(value = "本行A品券优惠单品额度")
    private BigDecimal topCouponAmount;

//    @ApiModelProperty(value = "退回赠品额度")
//    private BigDecimal complimentaryAmount;

    @ApiModelProperty(value = "退积分金额")
    private BigDecimal complimentaryAmount;

    @ApiModelProperty(value = "服纺券单品金额")
    private BigDecimal returnClothingSpinning;

    @ApiModelProperty(value = "批次编码")
    private String batchInfoCode;

    @ApiModelProperty(value = "批次号时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private String batchDate;

    @ApiModelProperty(value = "批次号")
    private String batchCode;

    //结算系统使用
    @ApiModelProperty(value = "商品品牌编码")
    @JsonProperty("product_brand_code")
    private String productBrandCode;

    @ApiModelProperty(value = "商品品牌名称")
    @JsonProperty("product_brand_name")
    private String productBrandName;

    @ApiModelProperty(value = "商品品类编码")
    @JsonProperty("product_category_codes")
    private String productCategoryCodes;

    @ApiModelProperty(value = "商品品类名称")
    @JsonProperty("product_category_names")
    private String productCategoryNames;

    @ApiModelProperty(value = "商品属性编码")
    @JsonProperty("product_property_code")
    private String productPropertyCode;

    @ApiModelProperty(value = "商品属性名称")
    @JsonProperty("product_property_name")
    private String productPropertyName;

    @ApiModelProperty(value = "含税采购价（元）")
    @JsonProperty("purchase_amount")
    private BigDecimal purchaseAmount;

    @ApiModelProperty(value="批次列表")
    @JsonProperty("batch_list")
    private List<ErpBatchInfo> batchList;

    @ApiModelProperty(value="子订单编码")
    @JsonProperty("order_code")
    private String orderCode;

    @ApiModelProperty(value="渠道售价（分销价）")
    @JsonProperty("price_tax")
    private BigDecimal priceTax;

    @ApiModelProperty(value="活动价")
    @JsonProperty("activity_price")
    private BigDecimal activityPrice;

    @ApiModelProperty(value="实发商品数量")
    @JsonProperty("actual_product_count")
    private Integer actualProductCount;

    @ApiModelProperty(value="分摊后金额")
    @JsonProperty("total_preferential_amount")
    private BigDecimal totalPreferentialAmount;

    @ApiModelProperty(value="活动优惠")
    @JsonProperty("total_acivity_amount")
    private BigDecimal totalAcivityAmount;

    @ApiModelProperty(value="销项税率")
    @JsonProperty("output_tax_rate")
    private BigDecimal outputTaxRate;
}