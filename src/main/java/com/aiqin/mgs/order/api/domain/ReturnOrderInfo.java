package com.aiqin.mgs.order.api.domain;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@ApiModel("退货商品信息表")
public class ReturnOrderInfo {

    @ApiModelProperty(value = "主键id")
//    @JsonProperty("id")
    private Long id;

    @ApiModelProperty(value = "业务id")
//    @JsonProperty("return_order_id")
    private String returnOrderId;

    @ApiModelProperty(value = "退货单号")
//    @JsonProperty("return_order_code")
    private String returnOrderCode;

    @ApiModelProperty(value = "订单号")
//    @JsonProperty("order_store_code")
    private String orderStoreCode;

    @ApiModelProperty(value = "公司编码")
//    @JsonProperty("company_code")
    private String companyCode;

    @ApiModelProperty(value = "公司名称")
//    @JsonProperty("company_name")
    private String companyName;

    @ApiModelProperty(value = "供应商编码")
//    @JsonProperty("supplier_code")
    private String supplierCode;

    @ApiModelProperty(value = "供应商名称")
//    @JsonProperty("supplier_name")
    private String supplierName;

    @ApiModelProperty(value = "仓库编码")
//    @JsonProperty("transport_center_code")
    private String transportCenterCode;

    @ApiModelProperty(value = "仓库名称")
//    @JsonProperty("transport_center_name")
    private String transportCenterName;

    @ApiModelProperty(value = "库房编码")
//    @JsonProperty("warehouse_code")
    private String warehouseCode;

    @ApiModelProperty(value = "库房名称")
//    @JsonProperty("warehouse_name")
    private String warehouseName;

    @ApiModelProperty(value = "采购组编码")
//    @JsonProperty("purchase_group_code")
    private String purchaseGroupCode;

    @ApiModelProperty(value = "采购组名称")
//    @JsonProperty("purchase_group_name")
    private String purchaseGroupName;

    @ApiModelProperty(value = "1-待审核，2-审核通过，3-订单同步中，4-等待退货验收，5-等待退货入库，6-等待审批，11-退货完成，12-退款完成，97-退货终止，98-审核不通过，99-已取消")
//    @JsonProperty("return_order_status")
    private Integer returnOrderStatus;

//    @ApiModelProperty(value = "订单类型 0直送、1配送、2辅采")
    @ApiModelProperty(value = "订单类型 1直送 2配送 3货架")
//    @JsonProperty("order_type")
    private Integer orderType;

    @ApiModelProperty(value = "退货类型  0客户退货、1缺货退货、2售后退货、3冲减单")
//    @JsonProperty("return_order_type")
    private Integer returnOrderType;

    @ApiModelProperty(value = "客户编码")
//    @JsonProperty("customer_code")
    private String customerCode;

    @ApiModelProperty(value = "客户名称")
//    @JsonProperty("customer_name")
    private String customerName;

    @ApiModelProperty(value = "是否锁定  0是 1否")
//    @JsonProperty("return_lock")
    private Integer returnLock;

    @ApiModelProperty(value = "锁定原因")
//    @JsonProperty("return_reason")
    private String returnReason;

    @ApiModelProperty(value = "支付状态  0 已支付 1未支付")
//    @JsonProperty("payment_status")
    private Integer paymentStatus;

    @ApiModelProperty(value = "支付方式编码")
//    @JsonProperty("payment_code")
    private String paymentCode;

    @ApiModelProperty(value = "支付方式名称")
//    @JsonProperty("payment_name")
    private String paymentName;

    @ApiModelProperty(value = "处理办法 1--退货退款(通过) 2--挂账 3--不通过(驳回) 4--仅退款 5--A品挂账  6退赠品额度  99--已取消")
//    @JsonProperty("treatment_method")
    private Integer treatmentMethod;

    @ApiModelProperty(value = "物流公司编码")
//    @JsonProperty("logistics_company_code")
    private String logisticsCompanyCode;

    @ApiModelProperty(value = "物流公司名称")
//    @JsonProperty("logistics_company_name")
    private String logisticsCompanyName;

    @ApiModelProperty(value = "物流单号")
//    @JsonProperty("logistics_code")
    private String logisticsCode;

    @ApiModelProperty(value = "收货时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date deliveryTime;

    @ApiModelProperty(value = "预计发货时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date preExpectTime;

    @ApiModelProperty(value = "发运时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date transportTime;

    @ApiModelProperty(value = "收货时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date receiveTime;

    @ApiModelProperty(value = "商品数量")
//    @JsonProperty("product_count")
    private Long productCount;

    @ApiModelProperty(value = "有效期",example = "2001-01-01 01:01:01")
//    @JsonProperty("valid_time")
    private Date validTime;

    @ApiModelProperty(value = "退货金额")
//    @JsonProperty("return_order_amount")
    private BigDecimal returnOrderAmount;

    @ApiModelProperty(value = "实退商品数量")
//    @JsonProperty("actual_product_count")
    private Long actualProductCount;

    @ApiModelProperty(value = "实退商品总金额")
//    @JsonProperty("actual_return_order_amount")
    private BigDecimal actualReturnOrderAmount;

    @ApiModelProperty(value = "配送方式编码")
//    @JsonProperty("distribution_mode_code")
    private String distributionModeCode;

    @ApiModelProperty(value = "配送方式名称")
//    @JsonProperty("distribution_mode_name")
    private String distributionModeName;

    @ApiModelProperty(value = "收货人")
//    @JsonProperty("receive_person")
    private String receivePerson;

    @ApiModelProperty(value = "收货人电话")
//    @JsonProperty("receive_mobile")
    private String receiveMobile;

    @ApiModelProperty(value = "运费")
//    @JsonProperty("deliver_amount")
    private BigDecimal deliverAmount;

    @ApiModelProperty(value = "邮编")
//    @JsonProperty("zip_code")
    private String zipCode;

    @ApiModelProperty(value = "重量")
//    @JsonProperty("total_weight")
    private Long totalWeight;

    @ApiModelProperty(value = "实际重量")
//    @JsonProperty("actual_total_weight")
    private Long actualTotalWeight;

    @ApiModelProperty(value = "体积")
//    @JsonProperty("total_volume")
    private Long totalVolume;

    @ApiModelProperty(value = "实退体积")
//    @JsonProperty("actual_total_volume")
    private Long actualTotalVolume;

    @ApiModelProperty(value = "收货区域 :省编码")
//    @JsonProperty("province_id")
    private String provinceId;

    @ApiModelProperty(value = "收货区域 :省")
//    @JsonProperty("province_name")
    private String provinceName;

    @ApiModelProperty(value = "收货区域 :市编码")
//    @JsonProperty("city_id")
    private String cityId;

    @ApiModelProperty(value = "收货区域 :市")
//    @JsonProperty("city_name")
    private String cityName;

    @ApiModelProperty(value = "收货区域 :区/县编码")
//    @JsonProperty("district_id")
    private String districtId;

    @ApiModelProperty(value = "收货区域 :区/县")
//    @JsonProperty("district_name")
    private String districtName;

    @ApiModelProperty(value = "收货地址")
//    @JsonProperty("receive_address")
    private String receiveAddress;

    @ApiModelProperty(value = "出库时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date outStockTime;

    @ApiModelProperty(value = "完成时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date finishTime;

    @ApiModelProperty(value = "退货原因编码 14:质量问题 15:无理由退货 16:物流破损")
//    @JsonProperty("return_reason_code")
    private String returnReasonCode;

    @ApiModelProperty(value = "退货原因描述")
//    @JsonProperty("return_reason_content")
    private String returnReasonContent;

    @ApiModelProperty(value = "验货备注")
//    @JsonProperty("inspection_remark")
    private String inspectionRemark;

    @ApiModelProperty(value = "备注")
//    @JsonProperty("remark")
    private String remark;

    @ApiModelProperty(value = "0. 启用   1.禁用")
//    @JsonProperty("use_status")
    private Integer useStatus;

    @ApiModelProperty(value = "创建人编码")
//    @JsonProperty("create_by_id")
    private String createById;

    @ApiModelProperty(value = "创建人名称")
//    @JsonProperty("create_by_name")
    private String createByName;

    @ApiModelProperty(value = "修改人编码")
//    @JsonProperty("update_by_id")
    private String updateById;

    @ApiModelProperty(value = "修改人名称")
//    @JsonProperty("update_by_name")
    private String updateByName;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty(value = "修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    @ApiModelProperty(value = "门店类型名称")
//    @JsonProperty("nature_name")
    private String natureName;

    @ApiModelProperty(value = "门店id")
//    @JsonProperty("store_id")
    private String storeId;

    @ApiModelProperty(value = "门店名称")
//    @JsonProperty("store_name")
    private String storeName;

    @ApiModelProperty(value = "门店编码")
//    @JsonProperty("store_code")
    private String storeCode;

    @ApiModelProperty(value = "门店类型编码")
//    @JsonProperty("nature_code")
    private String natureCode;

    @ApiModelProperty(value = "退款状态，0-未退款、1-已退款")
//    @JsonProperty("refund_status")
    private Integer refundStatus;

    @ApiModelProperty(value = "处理方式，0-整单退、1-部分退")
//    @JsonProperty("process_type")
    private Integer processType;

    @ApiModelProperty(value = "退回优惠额度")
//    @JsonProperty("return_discount_amount")
    private BigDecimal returnDiscountAmount;

    @ApiModelProperty(value = "门店余额")
//    @JsonProperty("store_balance_amount")
    private BigDecimal storeBalanceAmount;

    @ApiModelProperty(value = "门店剩余优惠额")
//    @JsonProperty("store_discount_amount")
    private BigDecimal storeDiscountAmount;

    @ApiModelProperty(value = "门店剩余授信额度")
//    @JsonProperty("store_creditLine")
    private String storeCreditLine;

    @ApiModelProperty(value = "审核备注")
//    @JsonProperty("review_remark")
    private String reviewRemark;

    @ApiModelProperty(value = "审核人")
//    @JsonProperty("review_operator")
    private String reviewOperator;

    @ApiModelProperty(value = "退回优惠额度信息")
//    @JsonProperty("discount_amount_infos")
    private String discountAmountInfos;

    @ApiModelProperty(value = "来源类型:1-web收银台 2-安卓收银台 3-微信公众号")
//    @JsonProperty("source_type")
    private Integer sourceType;

    @ApiModelProperty(value = "退款方式 1:现金 2:微信 3:支付宝 4:银联 5:退到加盟商账户")
//    @JsonProperty("returnMoney_type")
    private Integer returnMoneyType;

    @ApiModelProperty(value = "订单类别：1：收单配送 2：首单赠送 3：配送补货 4：首单直送 5：直送补货")
//    @JsonProperty("order_category")
    private Integer orderCategory;

    @ApiModelProperty(value = "审核人编码")
//    @JsonProperty("review_operator_id")
    private String reviewOperatorId;

    @ApiModelProperty(value = "审核时间")
//    @JsonProperty("review_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date reviewTime;

    @ApiModelProperty(value = "同步是否成功（创建退供单） 0 不生成采购单 1 待生成采购单 2采购单生成成功")
    private Integer orderSuccess;

    @ApiModelProperty(value = "是否真的发起退货 0:预生成退货单 1:原始订单全部发货完成生成退货单")
    private Integer reallyReturn;

    @ApiModelProperty(value = "退货金额")
    private BigDecimal returnGoodAmount;

    @ApiModelProperty(value = "实际退货金额")
    private BigDecimal actualReturnGoodAmount;

    @ApiModelProperty(value = "退回赠品额度")
    private BigDecimal complimentaryAmount;
}