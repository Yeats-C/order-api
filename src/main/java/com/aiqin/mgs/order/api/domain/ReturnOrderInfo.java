package com.aiqin.mgs.order.api.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@ApiModel("退货商品信息表")
public class ReturnOrderInfo {

    @ApiModelProperty(value = "主键id")
    private Long id;

    @ApiModelProperty(value = "业务id")
    private String returnOrderId;

    @ApiModelProperty(value = "退货单号")
    private String returnOrderCode;

    @ApiModelProperty(value = "订单号")
    private String orderStoreCode;

    @ApiModelProperty(value = "公司编码")
    private String companyCode;

    @ApiModelProperty(value = "公司名称")
    private String companyName;

    @ApiModelProperty(value = "供应商编码")
    private String supplierCode;

    @ApiModelProperty(value = "供应商名称")
    private String supplierName;

    @ApiModelProperty(value = "仓库编码")
    private String transportCenterCode;

    @ApiModelProperty(value = "仓库名称")
    private String transportCenterName;

    @ApiModelProperty(value = "库房编码")
    private String warehouseCode;

    @ApiModelProperty(value = "库房名称")
    private String warehouseName;

    @ApiModelProperty(value = "采购组编码")
    private String purchaseGroupCode;

    @ApiModelProperty(value = "采购组名称")
    private String purchaseGroupName;

    @ApiModelProperty(value = "1-待审核，2-审核通过，3-订单同步中，4-等待退货验收，5-等待退货入库，6-等待审批，11-退货完成，12-退款完成，97-退货终止，98-审核不通过，99-已取消")
    private Integer returnOrderStatus;

    @ApiModelProperty(value = "订单类型 0直送、1配送、2辅采")
    private Integer orderType;

    @ApiModelProperty(value = "退货类型  0客户退货、1缺货退货、2售后退货")
    private Integer returnOrderType;

    @ApiModelProperty(value = "客户编码")
    private String customerCode;

    @ApiModelProperty(value = "客户名称")
    private String customerName;

    @ApiModelProperty(value = "是否锁定  0是 1否")
    private Integer returnLock;

    @ApiModelProperty(value = "锁定原因")
    private String returnReason;

    @ApiModelProperty(value = "支付状态  0 已支付 1未支付")
    private Integer paymentStatus;

    @ApiModelProperty(value = "支付方式编码")
    private String paymentCode;

    @ApiModelProperty(value = "支付方式名称")
    private String paymentName;

    @ApiModelProperty(value = "处理办法 1--通过 2--挂账 3--不通过(驳回) 4--仅退款")
    private Integer treatmentMethod;

    @ApiModelProperty(value = "物流公司编码")
    private String logisticsCompanyCode;

    @ApiModelProperty(value = "物流公司名称")
    private String logisticsCompanyName;

    @ApiModelProperty(value = "物流单号")
    private String logisticsCode;

    @ApiModelProperty(value = "发货时间")
    private Date deliveryTime;

    @ApiModelProperty(value = "预计发货时间")
    private Date preExpectTime;

    @ApiModelProperty(value = "发运时间")
    private Date transportTime;

    @ApiModelProperty(value = "收货时间")
    private Date receiveTime;

    @ApiModelProperty(value = "商品数量")
    private Long productCount;

    @ApiModelProperty(value = "退货金额")
    private BigDecimal returnOrderAmount;

    @ApiModelProperty(value = "实退商品数量")
    private Long actualProductCount;

    @ApiModelProperty(value = "实退商品总金额")
    private BigDecimal actualReturnOrderAmount;

    @ApiModelProperty(value = "配送方式编码")
    private String distributionModeCode;

    @ApiModelProperty(value = "配送方式名称")
    private String distributionModeName;

    @ApiModelProperty(value = "收货人")
    private String receivePerson;

    @ApiModelProperty(value = "收货人电话")
    private String receiveMobile;

    @ApiModelProperty(value = "运费")
    private BigDecimal deliverAmount;

    @ApiModelProperty(value = "邮编")
    private String zipCode;

    @ApiModelProperty(value = "重量")
    private Long totalWeight;

    @ApiModelProperty(value = "实际重量")
    private Long actualTotalWeight;

    @ApiModelProperty(value = "体积")
    private Long totalVolume;

    @ApiModelProperty(value = "实退体积")
    private Long actualTotalVolume;

    @ApiModelProperty(value = "收货区域 :省编码")
    private String provinceId;

    @ApiModelProperty(value = "收货区域 :省")
    private String provinceName;

    @ApiModelProperty(value = "收货区域 :市编码")
    private String cityId;

    @ApiModelProperty(value = "收货区域 :市")
    private String cityName;

    @ApiModelProperty(value = "收货区域 :区/县编码")
    private String districtId;

    @ApiModelProperty(value = "收货区域 :区/县")
    private String districtName;

    @ApiModelProperty(value = "收货地址")
    private String receiveAddress;

    @ApiModelProperty(value = "有效期")
    private Date validTime;

    @ApiModelProperty(value = "出库时间")
    private Date outStockTime;

    @ApiModelProperty(value = "完成时间")
    private Date finishTime;

    @ApiModelProperty(value = "退货原因编码 14:质量问题 15:无理由退货 16:物流破损")
    private String returnReasonCode;

    @ApiModelProperty(value = "退货原因描述")
    private String returnReasonContent;

    @ApiModelProperty(value = "验货备注")
    private String inspectionRemark;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "0. 启用   1.禁用")
    private Integer useStatus;

    @ApiModelProperty(value = "创建人编码")
    private String createById;

    @ApiModelProperty(value = "创建人名称")
    private String createByName;

    @ApiModelProperty(value = "修改人编码")
    private String updateById;

    @ApiModelProperty(value = "修改人名称")
    private String updateByName;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "修改时间")
    private Date updateTime;

    @ApiModelProperty(value = "门店类型名称")
    private String natureName;

    @ApiModelProperty(value = "门店id")
    private String storeId;

    @ApiModelProperty(value = "门店名称")
    private String storeName;

    @ApiModelProperty(value = "门店编码")
    private String storeCode;

    @ApiModelProperty(value = "门店类型编码")
    private String natureCode;

    @ApiModelProperty(value = "退款状态，0-未退款、1-已退款")
    private Integer refundStatus;

    @ApiModelProperty(value = "处理方式，0-整单退、1-部分退")
    private Integer processType;

    @ApiModelProperty(value = "退回优惠额度")
    private BigDecimal returnDiscountAmount;

    @ApiModelProperty(value = "门店余额")
    private BigDecimal storeBalanceAmount;

    @ApiModelProperty(value = "门店剩余优惠额")
    private BigDecimal storeDiscountAmount;

    @ApiModelProperty(value = "门店剩余授信额度")
    private String storeCreditLine;

    @ApiModelProperty(value = "审核备注")
    private String reviewRemark;

    @ApiModelProperty(value = "审核人")
    private String reviewOperator;

    @ApiModelProperty(value = "退回优惠额度信息")
    private String discountAmountInfos;

    @ApiModelProperty(value = "来源类型:1-web收银台 2-安卓收银台 3-微信公众号")
    private Integer sourceType;

    @ApiModelProperty(value = "退款方式 1:现金 2:微信 3:支付宝 4:银联 5:退到加盟商账户")
    private Integer returnMoneyType;

    @ApiModelProperty(value = "订单类别：1：收单配送 2：首单赠送 3：配送补货 4：首单直送 5：直送补货")
    private Integer orderCategory;

}