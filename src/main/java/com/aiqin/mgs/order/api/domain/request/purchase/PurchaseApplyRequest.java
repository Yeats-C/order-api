package com.aiqin.mgs.order.api.domain.request.purchase;


import com.aiqin.mgs.order.api.base.PagesRequest;
import com.aiqin.mgs.order.api.domain.response.PurchaseGroupVo;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Data
public class PurchaseApplyRequest extends PagesRequest {

    @ApiModelProperty(value="采购申请单id")
    @JsonProperty("purchase_apply_id")
    private String purchaseApplyId;

    @ApiModelProperty(value="采购申请单")
    @JsonProperty("purchase_apply_code")
    private String purchaseApplyCode;

    @ApiModelProperty(value="采购申请类型 0 手动 1自动")
    @JsonProperty("apply_type")
    private Integer applyType;

    @ApiModelProperty(value="采购申请状态0. 待提交 1.已完成  2.待审核 3.审核中 4.审核通过 5.审核不通过 6.撤销")
    @JsonProperty("apply_status")
    private Integer applyStatus;

    @ApiModelProperty(value="采购组编码")
    @JsonProperty("purchase_group_code")
    private String purchaseGroupCode;

    @ApiModelProperty(value="开始时间")
    @JsonProperty("begin_time")
    private String beginTime;

    @ApiModelProperty(value="结束时间")
    @JsonProperty("finish_time")
    private String finishTime;

    @ApiModelProperty(value="sku编号")
    @JsonProperty("sku_code")
    private String skuCode;

    @ApiModelProperty(value="sku名称")
    @JsonProperty("sku_name")
    private String skuName;

    @ApiModelProperty(value="spu编码")
    @JsonProperty("spu_code")
    private String spuCode;

    @ApiModelProperty(value="spu名称")
    @JsonProperty("product_name")
    private String productName;

    @ApiModelProperty(value="供应商编码")
    @JsonProperty("supplier_code")
    private String supplierCode;

    @ApiModelProperty(value="仓库编码")
    @JsonProperty("transport_center_code")
    private String transportCenterCode;

    @ApiModelProperty(value="品牌id")
    @JsonProperty("brand_id")
    private String brandId;

    @ApiModelProperty(value="品类id")
    @JsonProperty("category_id")
    private String categoryId;

    @ApiModelProperty(value="品牌名称")
    @JsonProperty("brand_name")
    private String brandName;

    @ApiModelProperty(value="品类名称")
    @JsonProperty("category_name")
    private String categoryName;

    @ApiModelProperty(value="商品属性编码")
    @JsonProperty("product_property_code")
    private String productPropertyCode;

    @ApiModelProperty(value="商品属性名称")
    @JsonProperty("product_property_name")
    private String productPropertyName;

    @ApiModelProperty(value="14大A品建议补货传值类型是否有值：0.是 1.否")
    @JsonProperty("a_replenish_type")
    private Integer aReplenishType;

    @ApiModelProperty(value="14大A品建议补货")
    @JsonProperty("a_replenish")
    private List<String> aReplenish;

    @ApiModelProperty(value="畅销商品建议补货传值类型是否有值：0.是 1.否")
    @JsonProperty("product_replenish_type")
    private Integer productReplenishType;

    @ApiModelProperty(value="畅销商品建议补货")
    @JsonProperty("product_replenish")
    private List<String> productReplenish;

    @ApiModelProperty(value="14大A品缺货传值类型是否有值：0.是 1.否")
    @JsonProperty("a_shortage_type")
    private Integer aShortageType;

    @ApiModelProperty(value="14大A品缺货")
    @JsonProperty("a_shortage")
    private List<String> aShortage;

    @ApiModelProperty(value="畅销商品缺货传值类型是否有值：0.是 1.否")
    @JsonProperty("product_shortage_type")
    private Integer productShortageType;

    @ApiModelProperty(value="畅销商品缺货")
    @JsonProperty("product_shortage")
    private List<String> productShortage;

    @ApiModelProperty(value="采购单号")
    @JsonProperty("purchase_order_code")
    private String purchaseOrderCode;

    @ApiModelProperty(value="库房编码")
    @JsonProperty("warehouse_code")
    private String warehouseCode;

    @ApiModelProperty(value="采购单状态 0.待审核 1.审核中 2.审核通过  3.备货确认 4.发货确认  5.入库开始 6.入库中 7.已入库  8.完成 9.取消 10.审核不通过")
    @JsonProperty("purchase_order_status")
    private Integer purchaseOrderStatus;

    @ApiModelProperty(value="仓储状态 0.未开始  1.确认中 2.完成")
    @JsonProperty("storage_status")
    private Integer storageStatus;

    @ApiModelProperty(value="采购方式 0 配送  1.铺采直送")
    @JsonProperty("purchase_mode")
    private Integer purchaseMode;

    @ApiModelProperty(value="不需要传的参数")
    private List<PurchaseGroupVo> groupList;

    @ApiModelProperty(value="关联审批单")
    @JsonProperty("approval_code")
    private String approvalCode;

    // @ApiModelProperty(value="去重商品查询")
    // @JsonProperty("search_list")
    // private List<PurchaseProductSearchRequest>  searchList;

    @ApiModelProperty(value="采购单类型编码 1 普通采购 2 预采购")
    @JsonProperty("purchase_order_type_code")
    private Integer purchaseOrderTypeCode;

    @ApiModelProperty(value="创建开始时间")
    @JsonProperty("create_begin_time")
    private String createBeginTime;

    @ApiModelProperty(value="创建结束时间")
    @JsonProperty("create_finish_time")
    private String createFinishTime;

    @ApiModelProperty(value="修改开始时间")
    @JsonProperty("update_begin_time")
    private String updateBeginTime;

    @ApiModelProperty(value="修改结束时间")
    @JsonProperty("update_finish_time")
    private String updateFinishTime;

    @ApiModelProperty(value="供应商名称")
    @JsonProperty("supplier_name")
    private String supplierName;

    @ApiModelProperty(value="预采购类型 0 普通采购 1.预采购")
    @JsonProperty("pre_purchase_type")
    private Integer prePurchaseType;

    @ApiModelProperty(value="采购价来源 0.读取 1.录入/ 采购单来源 0.采购申请 1.订单")
    @JsonProperty("purchase_source")
    private Integer purchaseSource;

    @ApiModelProperty(value="公司编码")
    @JsonProperty("company_code")
    private String companyCode;

    public PurchaseApplyRequest() {
    }

    public PurchaseApplyRequest(String createBeginTime, String createFinishTime, String updateBeginTime, String updateFinishTime,
                                String purchaseGroupCode, String purchaseApplyCode, String supplierCode, String supplierName, Integer prePurchaseType,
                                Integer applyType, Integer purchaseSource, Integer applyStatus, String companyCode) {
        this.createBeginTime = createBeginTime;
        this.createFinishTime = createFinishTime;
        this.updateBeginTime = updateBeginTime;
        this.updateFinishTime = updateFinishTime;
        this.purchaseGroupCode = purchaseGroupCode;
        this.purchaseApplyCode = purchaseApplyCode;
        this.supplierCode = supplierCode;
        this.supplierName = supplierName;
        this.prePurchaseType = prePurchaseType;
        this.applyType = applyType;
        this.purchaseSource = purchaseSource;
        this.applyStatus = applyStatus;
        this.companyCode = companyCode;
    }

    public PurchaseApplyRequest(String purchaseApplyId, String purchaseGroupCode, String skuCode, String skuName, String spuCode, String productName,
                                String supplierCode, String transportCenterCode, String brandId, String brandName, String categoryId, String categoryName,
                                String productPropertyCode, String productPropertyName,Integer aReplenishType, Integer productReplenishType, Integer aShortageType,
                                Integer productShortageType) {
        this.purchaseApplyId = purchaseApplyId;
        this.purchaseGroupCode = purchaseGroupCode;
        this.skuCode = skuCode;
        this.skuName = skuName;
        this.spuCode = spuCode;
        this.productName = productName;
        this.supplierCode = supplierCode;
        this.transportCenterCode = transportCenterCode;
        this.brandId = brandId;
        this.brandName = brandName;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.productPropertyCode = productPropertyCode;
        this.productPropertyName = productPropertyName;
        this.aReplenishType = aReplenishType;
        this.productReplenishType = productReplenishType;
        this.aShortageType = aShortageType;
        this.productShortageType = productShortageType;
    }

    public PurchaseApplyRequest(String purchaseGroupCode, String createBeginTime, String createFinishTime, String updateBeginTime,
                                String updateFinishTime, String purchaseOrderCode, String supplierCode, String supplierName,
                                String transportCenterCode,  String warehouseCode, Integer purchaseOrderStatus,
                                Integer purchaseSource, String purchaseApplyCode, String companyCode ) {
        this.purchaseGroupCode = purchaseGroupCode;
        this.createBeginTime = createBeginTime;
        this.createFinishTime = createFinishTime;
        this.updateBeginTime = updateBeginTime;
        this.updateFinishTime = updateFinishTime;
        this.purchaseOrderCode = purchaseOrderCode;
        this.supplierCode = supplierCode;
        this.supplierName = supplierName;
        this.transportCenterCode = transportCenterCode;
        this.warehouseCode = warehouseCode;
        this.purchaseOrderStatus = purchaseOrderStatus;
        this.purchaseSource = purchaseSource;
        this.purchaseApplyCode = purchaseApplyCode;
        this.companyCode = companyCode;
    }
}
