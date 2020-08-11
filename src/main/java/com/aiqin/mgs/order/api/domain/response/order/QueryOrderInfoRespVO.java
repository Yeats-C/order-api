package com.aiqin.mgs.order.api.domain.response.order;

import com.aiqin.mgs.order.api.base.OrderStatus;
import com.aiqin.mgs.order.api.domain.OperationLog;
import com.aiqin.mgs.order.api.domain.response.returngoods.ReturnOrderInfoItemBatchRespVO;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@ApiModel("订单详情")
public class QueryOrderInfoRespVO {

    /** 订单信息*/
    @ApiModelProperty("订单编码(订单号)")
    private String orderCode;

    @ApiModelProperty("是否锁定(0是1否）")
    private Integer orderLock;

    @ApiModelProperty("是否是异常订单(0是1否)")
    private Integer orderException;

    @ApiModelProperty("订单状态")
    private String orderStatus;

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = OrderStatus.getAllStatus().get(orderStatus).getBackgroundOrderStatus();
    }

    @ApiModelProperty("支付状态(0已支付  1未支付)")
    private Integer paymentStatus;

    @ApiModelProperty("支付方式")
    private String paymentName;

    @ApiModelProperty("类型：1配送、2直送、3货架直送、4采购直送")
    private String orderTypeName;

    @ApiModelProperty("订单类别编码")
    private String orderCategoryCode;

    @ApiModelProperty("订单类别名称")
    private String orderCategoryName;

    @ApiModelProperty("父子订单(0父订单1子订单)")
    private Integer orderLevel;

    @ApiModelProperty("父订单号")
    private String mainOrderCode;

    @ApiModelProperty("供应商名称")
    private String supplierName;

    @ApiModelProperty("仓库名称")
    private String transportCenterName;

    @ApiModelProperty("库房名称")
    private String warehouseName;

    @ApiModelProperty("发票类型 1不开、2增普、3增专")
    private String invoiceType;

    @ApiModelProperty("发票抬头")
    private String invoiceTitle;

    @ApiModelProperty("运输公司名称")
    private String transportCompanyName;

    @ApiModelProperty("运输公司编码")
    private String transportCompanyCode;

    @ApiModelProperty("运输/物流单号")
    private String transportCode;

    @ApiModelProperty("支付日期")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private Date paymentTime;

    @ApiModelProperty("发货时间")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private Date deliveryTime;

    @ApiModelProperty("发运时间")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private Date transportTime;

    @ApiModelProperty("签收/收货时间")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private Date receiveTime;

    @ApiModelProperty("备注")
    private String remake;

    @ApiModelProperty("创建时间")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @ApiModelProperty("修改时间")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    @ApiModelProperty("修改人")
    private String updateByName;

    @ApiModelProperty("创建人")
    private String createByName;

    /** 客户信息及地址*/
    @ApiModelProperty("客户编码")
    private String customerCode;

    @ApiModelProperty("客户名称")
    private String customerName;

    @ApiModelProperty("配送方式")
    private String distributionModeName;

    @ApiModelProperty("收货人")
    private String receivePerson;

    @ApiModelProperty("收货人手机号")
    private String receiveMobile;

    @ApiModelProperty("邮编")
    private String zipCode;

    @ApiModelProperty("省名称")
    private String provinceName;

    @ApiModelProperty("市名称")
    private String cityName;

    @ApiModelProperty("区名称")
    private String districtName;

    @ApiModelProperty("详细地址")
    private String detailAddress;

    /** 渠道信息*/
    @ApiModelProperty("商品数量")
    private Long productCount;

    @ApiModelProperty("实发商品数量")
    private Long actualProductCount;

    @ApiModelProperty("重量")
    private Long totalWeight;

    @ApiModelProperty("体积")
    private Long totalVolume;

    @ApiModelProperty("实际重量")
    private Long actualTotalWeight;

    @ApiModelProperty("实际体积")
    private Long actualTotalVolume;

    @ApiModelProperty("商品总金额")
    private BigDecimal totalProductAmount;

    @ApiModelProperty("订单金额")
    private BigDecimal orderAmount;

    @ApiModelProperty("运费")
    private BigDecimal deliverAmount;

    @ApiModelProperty("实发商品总金额")
    private BigDecimal actualTotalProductAmount;

    @ApiModelProperty("减免比例")
    private Integer logisticsRemissionRatio;

    @ApiModelProperty("活动优惠")
    private BigDecimal activityDiscount;

    @ApiModelProperty("优惠额度")
    private BigDecimal discountAmount;

    @ApiModelProperty("实发订单金额")
    private BigDecimal actualOrderAmount;

    @ApiModelProperty("服纺券优惠金额")
    private BigDecimal suitCouponMoney;

    @ApiModelProperty("A品券优惠金额")
    private BigDecimal topCouponMoney;

    @ApiModelProperty("商品信息")
    private List<QueryOrderInfoItemRespVO> productList;

    @ApiModelProperty("商品批次信息")
    private List<ReturnOrderInfoItemBatchRespVO> productBatchList;

//    @ApiModelProperty("批次信息")
//    private List<QueryOrderInfoItemBatchRespVO> batchList;

    @ApiModelProperty("日志信息")
    private List<OperationLog> logs;
}
