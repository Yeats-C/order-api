package com.aiqin.mgs.order.api.domain.response.returngoods;

import com.aiqin.mgs.order.api.domain.OperationLog;
import com.aiqin.mgs.order.api.domain.OperationOrderLog;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@ApiModel("退货详情返回vo")
public class ReturnOrderDetailRespVO {

    /** 退货信息*/
    @ApiModelProperty("退货单号")
    @JsonProperty("return_order_code")
    private String returnOrderCode;

    @ApiModelProperty("订单编码(订单号)")
    @JsonProperty("order_store_code")
    private String orderStoreCode;

    @ApiModelProperty("状态1-待审核，2-审核通过，3-订单同步中，4-等待退货验收，5-等待退货入库，6-等待审批，11-退货完成，" +
            "12-退款完成，97-退货终止，98-审核不通过，99-已取消")
    @JsonProperty("return_order_status")
    private Integer returnOrderStatus;

    @ApiModelProperty("退货类型 0客户退货、1缺货退货、2售后退货、3冲减单 、4客户取消")
    @JsonProperty("return_order_type")
    private Integer returnOrderType;

    @ApiModelProperty("订单类型 1直送 2配送 3货架")
    @JsonProperty("order_type")
    private Integer orderType;

    @ApiModelProperty("锁定状态 0是 1否")
    @JsonProperty("return_lock")
    private Integer returnLock;

    @ApiModelProperty("支付状态  0 已支付 1未支付")
    @JsonProperty("payment_status")
    private Integer paymentStatus;

    @ApiModelProperty("支付方式")
    @JsonProperty("payment_name")
    private String paymentName;

    @ApiModelProperty("仓库")
    @JsonProperty("transport_center_name")
    private String transportCenterName;

    @ApiModelProperty("库房")
    @JsonProperty("warehouse_name")
    private String warehouseName;

    @ApiModelProperty("供应商")
    @JsonProperty("supplier_name")
    private String supplierName;

    @ApiModelProperty("处理办法 1--退货退款(通过) 2--挂账 3--不通过(驳回) 4--仅退款 99--已取消")
    @JsonProperty("treatment_method")
    private Integer treatmentMethod;

    @ApiModelProperty("公司/渠道")
    @JsonProperty("company_name")
    private String companyName;

    @ApiModelProperty("物流公司")
    @JsonProperty("logistics_company_name")
    private String logisticsCompanyName;

    @ApiModelProperty("物流单号")
    @JsonProperty("logistics_code")
    private String logisticsCode;

    @ApiModelProperty("发货时间")
    @JsonProperty("delivery_time")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private Date deliveryTime;

    @ApiModelProperty("收货时间")
    @JsonProperty("receive_time")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private Date receiveTime;

    @ApiModelProperty("创建时间")
    @JsonProperty("create_time")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @ApiModelProperty("操作时间")
    @JsonProperty("update_time")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    @ApiModelProperty("创建人")
    @JsonProperty("create_by_name")
    private String createByName;

    @ApiModelProperty("操作人")
    @JsonProperty("update_by_name")
    private String updateByName;

    @ApiModelProperty("退货原因")
    @JsonProperty("return_reason_content")
    private String returnReasonContent;

    @ApiModelProperty("备注")
    @JsonProperty("remark")
    private String remark;

    @ApiModelProperty("验货备注")
    @JsonProperty("inspection_remark")
    private String inspectionRemark;

    /** 客户信息及地址*/
    @ApiModelProperty("客户编码")
    @JsonProperty("customer_code")
    private String customerCode;

    @ApiModelProperty("客户名称")
    @JsonProperty("customer_name")
    private String customerName;

    @ApiModelProperty("配送方式")
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

    @ApiModelProperty(value = "省名称")
    @JsonProperty("province_name")
    private String provinceName;

    @ApiModelProperty(value = "市名称")
    @JsonProperty("city_name")
    private String cityName;

    @ApiModelProperty(value = "县名称")
    @JsonProperty("district_name")
    private String districtName;

    @ApiModelProperty(value = "收货地址")
    @JsonProperty("receive_address")
    private String receiveAddress;

    /** 渠道信息*/
    @ApiModelProperty(value = "商品数量")
    @JsonProperty("product_count")
    private Long productCount;

    @ApiModelProperty(value = "实退商品数量")
    @JsonProperty("actual_product_count")
    private Long actualProductCount;

    @ApiModelProperty(value = "重量")
    @JsonProperty("total_weight")
    private Long totalWeight;

    @ApiModelProperty(value = "实退重量")
    @JsonProperty("actual_total_weight")
    private Long actualTotalWeight;

    @ApiModelProperty(value = "体积")
    @JsonProperty("total_volume")
    private Long totalVolume;

    @ApiModelProperty(value = "实退体积")
    @JsonProperty("actual_total_volume")
    private Long actualTotalVolume;

    @ApiModelProperty(value = "商品总金额")
    @JsonProperty("return_order_amount")
    private BigDecimal returnOrderAmount;

    @ApiModelProperty(value = "实退商品总金额")
    @JsonProperty("actual_return_order_amount")
    private BigDecimal actualReturnOrderAmount;

    @ApiModelProperty(value = "退货金额")
    @JsonProperty("return_good_amount")
    private BigDecimal returnGoodAmount;

    @ApiModelProperty(value = "实际退货金额")
    @JsonProperty("actual_return_good_amount")
    private BigDecimal actualReturnGoodAmount;

    @ApiModelProperty("退货订单商品")
    @JsonProperty("item_list")
    private List<ReturnOrderInfoItemRespVO> itemList;

    @ApiModelProperty("退货订单批次商品")
    @JsonProperty("item_batch_list")
    private List<ReturnOrderInfoItemBatchRespVO> itemBatchList;

//    @ApiModelProperty("批次信息")
//    @JsonProperty("batch_list")
//    private List<ReturnOrderInfoApplyInboundDetailRespVO> batchList;

    @ApiModelProperty("日志记录")
    @JsonProperty("log_list")
    private List<OperationOrderLog> logList;
}
