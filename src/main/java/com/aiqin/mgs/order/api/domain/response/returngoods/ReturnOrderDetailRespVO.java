package com.aiqin.mgs.order.api.domain.response.returngoods;

import com.aiqin.mgs.order.api.base.ReturnOrderStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@ApiModel("退货详情返回vo")
public class ReturnOrderDetailRespVO {
    @ApiModelProperty("退货订单编码")
    private String returnOrderCode;

    @ApiModelProperty("订单编码(订单号)")
    private String orderCode;

    @ApiModelProperty("创建时间")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createDate;

    @ApiModelProperty("类型：直送、配送、首单、首单赠送")
    private String orderType;

    @ApiModelProperty("订单类型编码")
    private Integer orderTypeCode;

    @ApiModelProperty("退货类型：客户退货、缺货退货、售后退货")
    private String returnOrderType;

    @ApiModelProperty("退货类型编码")
    private Integer returnOrderTypeCode;

    @ApiModelProperty("支付状态0未支付1已支付")
    private Integer paymentStatus;

    @ApiModelProperty("是否锁定(0否1是）")
    private Integer beLock;

    @ApiModelProperty("是否删除(0否1是)")
    private Integer beDelete;

    @ApiModelProperty("物流中心名称")
    private String transportCenterName;

    @ApiModelProperty("仓库名称")
    private String warehouseName;

    @ApiModelProperty("供应商名称")
    private String supplierName;

    @ApiModelProperty("客户名称")
    private String customerName;

    @ApiModelProperty("客户名称")
    private String customerCode;

    @ApiModelProperty("收货人")
    private String consignee;

    @ApiModelProperty("收货人手机号")
    private String consigneePhone;

    @ApiModelProperty("地址")
    private String address;

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

    @ApiModelProperty("配送方式")
    private String distributionMode;

    @ApiModelProperty("订单状态(状态有点多，后面补)")
    private String orderStatus;

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = ReturnOrderStatus.getAllStatus().get(orderStatus).getBackgroundOrderStatus();
    }
    @ApiModelProperty("支付方式")
    private String paymentType;

    @ApiModelProperty("运费")
    private Long deliverAmount;

    @ApiModelProperty("商品数量")
    private Long productNum;

    @ApiModelProperty("商品总金额")
    private Long productTotalAmount;

    @ApiModelProperty("退货金额")
    private Long returnOrderAmount;

    @ApiModelProperty("重量")
    private Long weight;

    @ApiModelProperty("体积")
    private Long volume;

    @ApiModelProperty("发货时间")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private Date deliveryTime;

    @ApiModelProperty("收货时间")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private Date receivingTime;

    @ApiModelProperty("操作人")
    private String operator;

    @ApiModelProperty("操作时间")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private Date operatorTime;

    @ApiModelProperty("验货备注")
    private String inspectionRemark;

    @ApiModelProperty("公司编码")
    private String companyCode;

    @ApiModelProperty("公司名称")
    private String companyName;

    @ApiModelProperty("运输公司")
    private String transportCompany;

    @ApiModelProperty("运输单号")
    private String transportNumber;

    @ApiModelProperty("退货原因描述")
    private String returnReasonContent;

    @ApiModelProperty("备注")
    private String remake;

    @ApiModelProperty("处理办法（1退货退款2仅退货）")
    private Integer treatmentMethod;

    @ApiModelProperty("来源名称")
    private String orderOriginalName;

    @ApiModelProperty("来源编码")
    private String orderOriginal;

    @ApiModelProperty("创建人编码")
    private String createById;

    @ApiModelProperty("创建人名称")
    private String createByName;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("修改人编码")
    private String updateById;

    @ApiModelProperty("修改人名称")
    private String updateByName;

    @ApiModelProperty("修改时间")
    private Date updateTime;

    @ApiModelProperty("实际重量")
    private BigDecimal actualWeight;

    @ApiModelProperty("实际退货数量")
    private Long actualProductNum;

    @ApiModelProperty("实际体积")
    private BigDecimal actualVolume;

    @ApiModelProperty("实际分销价")
    private BigDecimal actualProductTotalAmount;

    @ApiModelProperty("实际退货金额")
    private BigDecimal actualReturnOrderAmount;

    @ApiModelProperty("渠道总金额")
    private BigDecimal productChannelTotalAmount;

    @ApiModelProperty("实际渠道（商品）总金额")
    private BigDecimal actualProductChannelTotalAmount;

    @ApiModelProperty("退货订单商品")
    private List<ReturnOrderInfoItemRespVO> itemList;

    @ApiModelProperty("批次信息")
    private List<ReturnOrderInfoApplyInboundDetailRespVO> detailList;
}
