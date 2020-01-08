package com.aiqin.mgs.order.api.domain.po.order;

import com.aiqin.mgs.order.api.component.enums.ErpOrderLevelEnum;
import com.aiqin.mgs.order.api.component.enums.ErpOrderStatusEnum;
import com.aiqin.mgs.order.api.component.enums.StatusEnum;
import com.aiqin.mgs.order.api.domain.response.order.ErpOrderOperationControlResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 订单实体
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/12/9 9:49
 */
@Data
@JsonInclude(JsonInclude.Include.ALWAYS)
public class ErpOrderInfo {

    @ApiModelProperty(value = "主键")
    @JsonProperty("id")
    private Long id;

    @ApiModelProperty(value = "订单id")
    @JsonProperty("order_store_id")
    private String orderStoreId;

    @ApiModelProperty(value = "订单编号")
    @JsonProperty("order_store_code")
    private String orderStoreCode;

    @ApiModelProperty(value = "公司编码")
    @JsonProperty("company_code")
    private String companyCode;

    @ApiModelProperty(value = "公司名称")
    @JsonProperty("company_name")
    private String companyName;

    @ApiModelProperty(value = "订单类型编码")
    @JsonProperty("order_type_code")
    private String orderTypeCode;

    @ApiModelProperty(value = "订单类型名称")
    @JsonProperty("order_type_name")
    private String orderTypeName;

    @ApiModelProperty(value = "订单类别编码")
    @JsonProperty("order_category_code")
    private String orderCategoryCode;

    @ApiModelProperty(value = "订单类别名称")
    @JsonProperty("order_category_name")
    private String orderCategoryName;

    @ApiModelProperty(value = "供应商编码")
    @JsonProperty("supplier_code")
    private String supplierCode;

    @JsonProperty("supplier_name")
    @ApiModelProperty(value = "供应商名称")
    private String supplierName;

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

    @ApiModelProperty(value = "客户编码")
    @JsonProperty("customer_code")
    private String customerCode;

    @ApiModelProperty(value = "客户名称")
    @JsonProperty("customer_name")
    private String customerName;

    @ApiModelProperty(value = "订单状态")
    @JsonProperty("order_status")
    private Integer orderStatus;

    @ApiModelProperty(value = "订单节点控制状态")
    @JsonProperty("order_node_status")
    private Integer orderNodeStatus;

    @ApiModelProperty(value = "订单状态描述")
    @JsonProperty("order_status_desc")
    private String orderStatusDesc;

    @ApiModelProperty(value = "是否是锁定的订单 0是 1否")
    @JsonProperty("order_lock")
    private Integer orderLock;

    @ApiModelProperty(value = "锁定原因")
    @JsonProperty("lock_reason")
    private String lockReason;

    @ApiModelProperty(value = "是否异常订单 0是 1否")
    @JsonProperty("order_exception")
    private Integer orderException;

    @ApiModelProperty(value = "异常原因")
    @JsonProperty("exception_reason")
    private String exceptionReason;

    @ApiModelProperty(value = "是否删除 0是 1否")
    @JsonProperty("order_delete")
    private Integer orderDelete;

    @ApiModelProperty(value = "支付状态 0已支付  1未支付")
    @JsonProperty("payment_status")
    private Integer paymentStatus;

    @ApiModelProperty(value = "支付状态描述")
    @JsonProperty("payment_status_desc")
    private String paymentStatusDesc;

    @ApiModelProperty(value = "省编码")
    @JsonProperty("province_id")
    private String provinceId;

    @ApiModelProperty(value = "省名称")
    @JsonProperty("province_name")
    private String provinceName;

    @ApiModelProperty(value = "市编码")
    @JsonProperty("city_id")
    private String cityId;

    @ApiModelProperty(value = "市名称")
    @JsonProperty("city_name")
    private String cityName;

    @ApiModelProperty(value = "区县编码")
    @JsonProperty("district_id")
    private String districtId;

    @ApiModelProperty(value = "区县名称")
    @JsonProperty("district_name")
    private String districtName;

    @ApiModelProperty(value = "收货地址")
    @JsonProperty("receive_address")
    private String receiveAddress;

    @ApiModelProperty(value = "配送方式编码")
    @JsonProperty("distribution_mode_code")
    private String distributionModeCode;

    @ApiModelProperty(value = "配送方式名称")
    @JsonProperty("distribution_mode_name")
    private String distributionModeName;

    @ApiModelProperty(value = "收货人")
    @JsonProperty("receive_person")
    private String receivePerson;

    @ApiModelProperty(value = "收货人电话")
    @JsonProperty("receive_mobile")
    private String receiveMobile;

    @ApiModelProperty(value = "邮编")
    @JsonProperty("zip_code")
    private String zipCode;

    @ApiModelProperty(value = "支付方式编码")
    @JsonProperty("payment_code")
    private String paymentCode;

    @ApiModelProperty(value = "支付方式名称")
    @JsonProperty("payment_name")
    private String paymentName;

    @ApiModelProperty(value = "运费")
    @JsonProperty("deliver_amount")
    private BigDecimal deliverAmount;

    @ApiModelProperty(value = "商品总价")
    @JsonProperty("total_product_amount")
    private BigDecimal totalProductAmount;

    @ApiModelProperty(value = "实际商品总价（发货商品总价）")
    @JsonProperty("actual_total_product_amount")
    private BigDecimal actualTotalProductAmount;

    @ApiModelProperty(value = "实际发货数量")
    @JsonProperty("actual_product_count")
    private Long actualProductCount;

    @ApiModelProperty(value = "优惠额度")
    @JsonProperty("discount_amount")
    private BigDecimal discountAmount;

    @ApiModelProperty(value = "实际支付金额")
    @JsonProperty("order_amount")
    private BigDecimal orderAmount;

    @ApiModelProperty(value = "付款日期")
    @JsonProperty("payment_time")
    private Date paymentTime;

    @ApiModelProperty(value = "发货时间")
    @JsonProperty("delivery_time")
    private Date deliveryTime;

    @ApiModelProperty(value = "发运时间")
    @JsonProperty("transport_time")
    private Date transportTime;

    @ApiModelProperty(value = "签收时间")
    @JsonProperty("receive_time")
    private Date receiveTime;

    @ApiModelProperty(value = "发运状态")
    @JsonProperty("transport_status")
    private Integer transportStatus;

    @ApiModelProperty(value = "发票类型 1不开 2增普 3增专")
    @JsonProperty("invoice_type")
    private Integer invoiceType;

    @ApiModelProperty(value = "发票抬头")
    @JsonProperty("invoice_title")
    private String invoiceTitle;

    @ApiModelProperty(value = "体积")
    @JsonProperty("total_volume")
    private BigDecimal totalVolume;

    @ApiModelProperty(value = "实际体积")
    @JsonProperty("actual_total_volume")
    private BigDecimal actualTotalVolume;

    @ApiModelProperty(value = "重量")
    @JsonProperty("total_weight")
    private BigDecimal totalWeight;

    @ApiModelProperty(value = "实际重量")
    @JsonProperty("actual_total_weight")
    private BigDecimal actualTotalWeight;

    @ApiModelProperty(value = "关联主订单号  如果是主订单，该字段存自己的订单号")
    @JsonProperty("main_order_code")
    private String mainOrderCode;

    @ApiModelProperty(value = "订单级别(0主1子订单)")
    @JsonProperty("order_level")
    private Integer orderLevel;

    @ApiModelProperty(value = "订单级别描述")
    @JsonProperty("order_level_desc")
    private String orderLevelDesc;

    @ApiModelProperty(value = "是否被拆分 0是 1否")
    @JsonProperty("split_status")
    private Integer splitStatus;

    /***申请取消时的状态*/
    @JsonProperty("before_cancel_status")
    private Integer beforeCancelStatus;

    @ApiModelProperty(value = "备注")
    @JsonProperty("remake")
    private String remake;

    @ApiModelProperty(value = "门店类型")
    @JsonProperty("store_type")
    private Integer storeType;

    @ApiModelProperty(value = "门店id")
    @JsonProperty("store_id")
    private String storeId;

    @ApiModelProperty(value = "门店编码")
    @JsonProperty("store_code")
    private String storeCode;

    @ApiModelProperty(value = "门店名称")
    @JsonProperty("store_name")
    private String storeName;

    @ApiModelProperty(value = "运输公司编码")
    @JsonProperty("transport_company_code")
    private String transportCompanyCode;

    @ApiModelProperty(value = "运输公司名称")
    @JsonProperty("transport_company_name")
    private String transportCompanyName;

    @ApiModelProperty(value = "运输单号，物流单号")
    @JsonProperty("transport_code")
    private String transportCode;

    @ApiModelProperty(value = "物流单id")
    @JsonProperty("logistics_id")
    private String logisticsId;

    @ApiModelProperty(value = "费用id")
    @JsonProperty("fee_id")
    private String feeId;

    @ApiModelProperty(value = "是否发生退货 0是 1否")
    @JsonProperty("order_return")
    private Integer orderReturn;

    @ApiModelProperty(value = "加盟商id")
    @JsonProperty("franchisee_id")
    private String franchiseeId;

    @ApiModelProperty(value = "加盟商编码")
    @JsonProperty("franchisee_code")
    private String franchiseeCode;

    @ApiModelProperty(value = "加盟商名称")
    @JsonProperty("franchisee_name")
    private String franchiseeName;

    @ApiModelProperty(value = "来源单号")
    @JsonProperty("source_code")
    private String sourceCode;

    @ApiModelProperty(value = "来源名称")
    @JsonProperty("source_name")
    private String sourceName;

    @ApiModelProperty(value = "来源类型")
    @JsonProperty("source_type")
    private Integer sourceType;

    @ApiModelProperty(value = "同步供应链状态")
    @JsonProperty("order_success")
    private Integer orderSuccess;

    @ApiModelProperty(value = "冲减单状态 1不需要生成冲减单 2待生成冲减单 3生成冲减单完成")
    @JsonProperty("scour_sheet_status")
    private Integer scourSheetStatus;

    @ApiModelProperty(value = "启用停用 0启用 1启用")
    @JsonProperty("use_status")
    private Integer useStatus;

    @ApiModelProperty(value = "创建人编码")
    @JsonProperty("create_by_id")
    private String createById;

    @ApiModelProperty(value = "创建人名称")
    @JsonProperty("create_by_name")
    private String createByName;

    @ApiModelProperty(value = "最近更新人编码")
    @JsonProperty("update_by_id")
    private String updateById;

    @ApiModelProperty(value = "最近更新人名称")
    @JsonProperty("update_by_name")
    private String updateByName;

    @ApiModelProperty(value = "创建时间")
    @JsonProperty("create_time")
    private Date createTime;

    @ApiModelProperty(value = "最近更新时间")
    @JsonProperty("update_time")
    private Date updateTime;

    @ApiModelProperty(value = "主订单费用详情")
    @JsonProperty("order_fee")
    private ErpOrderFee orderFee;

    @ApiModelProperty(value = "按钮组件控制")
    @JsonProperty("operation")
    private ErpOrderOperationControlResponse operation;

    @ApiModelProperty(value = "订单物流信息")
    @JsonProperty("order_logistics")
    private ErpOrderLogistics orderLogistics;

    @ApiModelProperty(value = "订单退款信息")
    @JsonProperty("order_refund")
    private ErpOrderRefund orderRefund;

    @ApiModelProperty(value = "订单商品明细行")
    @JsonProperty("item_list")
    private List<ErpOrderItem> itemList;

    @ApiModelProperty(value = "订单关联子订单列表")
    @JsonProperty("secondary_order_list")
    private List<ErpOrderInfo> secondaryOrderList;

    @ApiModelProperty(value = "订单日志")
    @JsonProperty("operation_log_list")
    private List<ErpOrderOperationLog> operationLogList;

    public String getOrderStatusDesc() {
        return ErpOrderStatusEnum.getEnumDesc(orderStatus);
    }

    public String getPaymentStatusDesc() {
        return StatusEnum.getPaymentStatusDesc(paymentStatus);
    }

    public String getOrderLevelDesc() {
        return ErpOrderLevelEnum.getEnumDesc(orderLevel);
    }
}
