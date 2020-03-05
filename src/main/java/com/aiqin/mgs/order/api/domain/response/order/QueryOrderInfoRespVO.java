package com.aiqin.mgs.order.api.domain.response.order;

import com.aiqin.mgs.order.api.base.OrderStatus;
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

    @ApiModelProperty("订单编码(订单号)")
    private String orderCode;

    @ApiModelProperty("类型：直送、配送、首单、首单赠送.辅采直送")
    private String orderType;

    @ApiModelProperty("客户名称")
    private String customerName;

    @ApiModelProperty("客户编码")
    private String customerCode;

    @ApiModelProperty("创建时间")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createDate;

    @ApiModelProperty("订单状态")
    private String orderStatus;

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = OrderStatus.getAllStatus().get(orderStatus).getBackgroundOrderStatus();
    }

    @ApiModelProperty("是否锁定(0否1是）")
    private Integer beLock;

    @ApiModelProperty("是否是异常订单(0否1是)")
    private Integer beException;

    @ApiModelProperty("支付状态(0未1已)")
    private Integer paymentStatus;

    @ApiModelProperty("物流中心名称")
    private String transportCenterName;

    @ApiModelProperty("仓库名称")
    private String warehouseName;

    @ApiModelProperty("供应商名称")
    private String supplierName;

    @ApiModelProperty("配送方式")
    private String distributionMode;

    @ApiModelProperty("收货人")
    private String consignee;

    @ApiModelProperty("收货人手机号")
    private String consigneePhone;

    @ApiModelProperty("省名称")
    private String provinceName;

    @ApiModelProperty("市名称")
    private String cityName;

    @ApiModelProperty("区名称")
    private String districtName;

    @ApiModelProperty("详细地址")
    private String detailAddress;

    @ApiModelProperty("邮编")
    private String zipCode;

    @ApiModelProperty("支付方式")
    private String paymentType;

    @ApiModelProperty("运费")
    private BigDecimal deliverAmount;

    @ApiModelProperty("商品分销价总金额")
    private BigDecimal productTotalAmount;

    @ApiModelProperty("商品渠道价总金额")
    private BigDecimal productChannelTotalAmount;

    @ApiModelProperty("优惠额度")
    private BigDecimal discountAmount;

    @ApiModelProperty("订单金额")
    private BigDecimal orderAmount;

    @ApiModelProperty("商品数量")
    private Long productNum;

    @ApiModelProperty("支付日期")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private Date paymentTime;

    @ApiModelProperty("发货时间")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private Date deliveryTime;

    @ApiModelProperty("发运时间")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private Date transportTime;

    @ApiModelProperty("收货时间")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private Date receivingTime;

    @ApiModelProperty("不开、增普、增专")
    private String invoiceType;

    @ApiModelProperty("发票抬头")
    private String invoiceTitle;

    @ApiModelProperty("操作人")
    private String operator;

    @ApiModelProperty("操作时间")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private Date operatorTime;

    @ApiModelProperty("活动优惠")
    private BigDecimal activityDiscount;

    @ApiModelProperty("重量")
    private Long weight;

    @ApiModelProperty("体积")
    private Long volume;

    @ApiModelProperty("是否父订单(0不是1是)")
    private Integer beMasterOrder;

    @ApiModelProperty("父订单号")
    private String masterOrderCode;

    @ApiModelProperty("订单来源")
    private String orderOriginal;

    @ApiModelProperty("备注")
    private String remake;

    @ApiModelProperty("门店类型编码")
    private String storeTypeCode;

    @ApiModelProperty("门店类型")
    private String storeType;

    @ApiModelProperty("订单类别名称")
    private String orderCategory;

    @ApiModelProperty("订单类别编码")
    private String orderCategoryCode;

    @ApiModelProperty("创建人")
    private String createByName;

    @ApiModelProperty("创建时间")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @ApiModelProperty("修改人")
    private String updateByName;

    @ApiModelProperty("修改时间")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    @ApiModelProperty("实际渠道总价")
    private BigDecimal actualProductChannelTotalAmount;

    @ApiModelProperty("实际订单金额")
    private BigDecimal actualOrderAmount;

    @ApiModelProperty("实际发货数量")
    private Long actualProductNum;

    @ApiModelProperty("订单来源名称")
    private String orderOriginalName;

    @ApiModelProperty("实际重量")
    private Long actualWeight;

    @ApiModelProperty("实际体积")
    private Long actualVolume;

    @ApiModelProperty("实际分销总价")
    private BigDecimal actualProductTotalAmount;

    @ApiModelProperty("减免比例")
    private Integer logisticsRemissionRatio;

    @ApiModelProperty("运输公司")
    private String transportCompany;

    @ApiModelProperty("运输公司编码")
    private String transportCompanyCode;

    @ApiModelProperty("运输单号")
    private String transportNumber;

    @ApiModelProperty("商品信息")
    private List<QueryOrderInfoItemRespVO> productList;

    @ApiModelProperty("批次信息")
    private List<QueryOrderInfoItemBatchRespVO> batchList;

    // @ApiModelProperty("日志信息")
    // private List<QueryOrderInfoLogRespVO> logs;
}
